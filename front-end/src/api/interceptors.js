import axios from "axios";
import logOut from "../utils/function/logout";
import ExceptionCode from "../utils/code/exceptionCode";

// axios 인스턴스 생성
const authAxios = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    Authorization: `Bearer ${localStorage.getItem("accessToken")}`
  }
})

const reissueAccessToken = async () => {
  const data = {
    gardenerId: localStorage.getItem("gardenerId"),
    refreshToken: localStorage.getItem("refreshToken")
  };

  const response = await axios.post(`${process.env.REACT_APP_API_URL}/token`, data)
    .catch((e) => {
      alert(e.response.data.message);
      logOut();
    });
  return response.data;
}

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
      const originRequest = error?.config;

      // token 재발급
      console.log("토큰 만료 -> reissue access token");
      const res = await reissueAccessToken(); // Token 클래스 받아와야함

      localStorage.setItem("accessToken", res.accessToken);
      localStorage.setItem("refreshToken", res.refreshToken); //RTR

      // 진행 중인 요청 이어하기
      return authAxios({
        ...originRequest,
        headers: {...originRequest.headers, Authorization: `Bearer ${res.accessToken}`},
        sent: true
      })

    } else if (errorCode == ExceptionCode.REFRESH_TOKEN_EXPIRED) { // Refresh Token 만료
      alert(error.response.data.message);
      logOut();
    } else if (errorCode == ExceptionCode.NO_TOKEN_IN_REDIS
      || errorCode == ExceptionCode.INVALID_REFRESH_TOKEN
      || errorCode == ExceptionCode.INVALID_JWT_TOKEN) {
      alert(errorCode + " : " + error.response.data.message);
      logOut();
    } else {
      alert(errorCode + " : " + error.response.data.message);
    }

    return Promise.reject(error.response.data);
  }
);

export default authAxios;
