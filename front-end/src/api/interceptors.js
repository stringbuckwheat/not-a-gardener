import axios from "axios";
import ExceptionCode from "../utils/code/exceptionCode";
import logOut from "../utils/function/logout";

const authAxios = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    Authorization: `Bearer ${localStorage.getItem("accessToken")}`
  }
});

let isTokenRefreshing = false;
let refreshSubscribers = []; // 재시도할 요청들

// Token 재발급 시 새로운 요청 대기
const onRefreshed = (accessToken) => {
  refreshSubscribers.forEach((callback) => callback(accessToken));
  refreshSubscribers = []; // 콜백 실행 후 배열 초기화
};

// 재시도할 요청들 추가
const addRefreshSubscriber = (callback) => {
  refreshSubscribers.push(callback);
};

const reissueAccessToken = async () => {
  const data = {
    gardenerId: localStorage.getItem("gardenerId"),
    refreshToken: localStorage.getItem("refreshToken")
  };

  if (!data.refreshToken) {
    throw new Error("No refresh token available");
  }

  const response = await axios.post(`${process.env.REACT_APP_API_URL}/token`, data);
  return response.data;
};

/**
 * Request Interceptor
 * : local storage에 accessToken 값이 있다면 헤더에 넣어준다.
 */
authAxios.interceptors.request.use(
  async (config) => {
    const accessToken = localStorage.getItem("accessToken");

    // local storage에 accessToken 값이 있다면 헤더에 넣어준다.
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  },

  (error) => Promise.reject(error)
);

/**
 * Response Interceptor
 * : 에러 처리 및 token 만료 로직
 */
authAxios.interceptors.response.use(
  (response) => response,
  async (error) => {
    console.log("=== error ===");
    console.log(error);

    if (!error.response || !error.response.data) {
      return Promise.reject(error);
    }

    const errorCode = error.response.data.code;

    if (errorCode === ExceptionCode.ACCESS_TOKEN_EXPIRED) {
      const originRequest = error.config;

      if (!isTokenRefreshing) {
        isTokenRefreshing = true;

        // token 재발급
        console.log("토큰 만료 -> reissue access token");
        try {
          const res = await reissueAccessToken();
          console.log("Silent Refresh 성공");

          localStorage.setItem("accessToken", res.accessToken);
          localStorage.setItem("refreshToken", res.refreshToken); //RTR
          isTokenRefreshing = false;
          onRefreshed(res.accessToken);

          return authAxios({
            ...originRequest,
            headers: {...originRequest.headers, Authorization: `Bearer ${res.accessToken}`},
            sent: true
          })

        } catch (err) {
          isTokenRefreshing = false;

          if (err.response?.data?.code === ExceptionCode.REFRESH_TOKEN_EXPIRED) { // Refresh Token 만료
            console.log("REFRESH_TOKEN_EXPIRED - AUTH AXIOS");
            await logOut();
          } else if ([
            ExceptionCode.NO_TOKEN_IN_REDIS,
            ExceptionCode.INVALID_REFRESH_TOKEN,
            ExceptionCode.INVALID_JWT_TOKEN
          ].includes(err.response?.data?.code)) {
            console.log("NO_TOKEN_IN_REDIS / INVALID_REFRESH_TOKEN / INVALID_JWT_TOKEN");

            alert(err.response.data.message);
            await logOut();
          }

          return Promise.reject(err);
        }
      } else {
        // 토큰 재발급 중인 경우 새로운 요청 대기
        return new Promise((resolve) => {
          addRefreshSubscriber((accessToken) => {
            resolve(authAxios({
              ...originRequest,
              headers: {...originRequest.headers, Authorization: `Bearer ${accessToken}`},
              sent: true
            }));
          });
        });
      }
    } else {
      alert(error.response.data.message);
    }

    return Promise.reject(error.response.data);
  }
);

export default authAxios;
