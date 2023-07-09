import axios from "axios";
import LogOut from "../utils/function/logout";

// axios 인스턴스 생성
const authAxios = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  timeout: 5000
})

/**
 * Request Interceptor
 * : local storage에 accessToken 값이 있다면 헤더에 넣어준다.
 */
authAxios.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem("login");

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
  (error) => {
    console.log("error", error);

    if (error.response.data.code === 401) {
      alert("로그인 시간이 만료되었습니다.")
      LogOut();
    }

    return Promise.reject(error);
  }
);

export default authAxios;
