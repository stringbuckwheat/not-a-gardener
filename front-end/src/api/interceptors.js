import axios from "axios";
import logOut from "../utils/function/logout";
import ExceptionCode from "../utils/code/exceptionCode";

// axios 인스턴스 생성
const authAxios = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    Authorization: `Bearer ${localStorage.getItem("accessToken")}`
  }
});

let isTokenRefreshing = false;
let refreshSubscribers = [];

// Token 재발급 시 새로운 요청 대기
const onRrefreshed = (accessToken) => {
  refreshSubscribers.forEach((callback) => callback(accessToken));
  refreshSubscribers = []; // 콜백 실행 후 배열 초기화
};

const addRefreshSubscriber = (callback) => {
  refreshSubscribers.push(callback);
};

const reissueAccessToken = async () => {
  const data = {
    gardenerId: localStorage.getItem("gardenerId"),
    refreshToken: localStorage.getItem("refreshToken")
  };

  const response = await axios.post(`${process.env.REACT_APP_API_URL}/token`, data);
  return response.data;
};

/**
 * Request Interceptor
 * : local storage에 accessToken 값이 있다면 헤더에 넣어준다.
 */
authAxios.interceptors.request.use(
  (config) => {
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

    const errorCode = error.response.data.code;

    if (errorCode === ExceptionCode.ACCESS_TOKEN_EXPIRED) {
      const originRequest = error.config;

      if (!isTokenRefreshing) {
        isTokenRefreshing = true;

        // token 재발급
        console.log("토큰 만료 -> reissue access token");
        const res = await reissueAccessToken().catch((err) => {
          isTokenRefreshing = false;

          if (err.response.data.code === ExceptionCode.REFRESH_TOKEN_EXPIRED) { // Refresh Token 만료
            console.log("REFRESH_TOKEN_EXPIRED - AUTH AXIOS");
            logOut();
          } else if (err.response.data.code === ExceptionCode.NO_TOKEN_IN_REDIS
            || err.response.data.code === ExceptionCode.INVALID_REFRESH_TOKEN
            || err.response.data.code === ExceptionCode.INVALID_JWT_TOKEN) {
            console.log("NO_TOKEN_IN_REDIS / INVALID_REFRESH_TOKEN / INVALID_JWT_TOKEN");

            alert(err.response.data.code + " : " + err.response.data.message);
            logOut();
          }

          return Promise.reject(err);
        });

        if (res) {
          console.log("Silent Refresh 성공");

          localStorage.setItem("accessToken", res.accessToken);
          localStorage.setItem("refreshToken", res.refreshToken); //RTR
          isTokenRefreshing = false;
          onRrefreshed(res.accessToken);
        }
      }

      return new Promise((resolve) => {
        addRefreshSubscriber((accessToken) => {
          originRequest.headers.Authorization = `Bearer ${accessToken}`;
          resolve(authAxios(originRequest));
        });
      });

    } else {
      alert(errorCode + " : " + error.response.data.message);
    }

    return Promise.reject(error.response.data);
  }
);

export default authAxios;
