import axios from "axios";
import LogOut from "../utils/function/logout";

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

    if (errorCode === "B001") {
      const originRequest = error?.config;

      // token 재발급
      console.log("토큰 만료 -> reissue access token");
      const newAccessToken = await reissueAccessToken(); // Token 클래스 받아와야함

      const accessToken = newAccessToken.accessToken;
      localStorage.setItem("accessToken", accessToken);

      // 진행 중인 요청 이어하기
      return authAxios({
        ...originRequest,
        headers: {...originRequest.headers, Authorization: `Bearer ${accessToken}`},
        sent: true
      })
    } else if(errorCode == "B002"){
      alert("로그인 시간이 만료되었어요.");
      LogOut();
    }

    return Promise.reject(error.response.data);
  }
);

export default authAxios;
