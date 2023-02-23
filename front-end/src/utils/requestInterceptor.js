import axios from "axios";

// axios 인스턴스 생성
const authAxios = axios.create({
    baseURL: 'http://localhost:3000',
    timeout: 1000
})

authAxios.interceptors.request.use(
    function (config) {
        const accessToken = localStorage.getItem("login");

        // local storage에 accessToken 값이 있다면 헤더에 넣어준다.
        if(accessToken) {
            config.headers.Authorization = `Bearer ${accessToken}`;
        }

        return config;
    },

    function (error) {
        return Promise.reject(error);
    }

);

export default authAxios;
