import axios from "axios";

const logOut = async () => {
  // 서버 redis 삭제
  await axios.post(`${process.env.REACT_APP_API_URL}/logout/${localStorage.getItem("gardenerId")}`, null);

  // localstorage 정리
  const provider = localStorage.getItem("provider");
  localStorage.clear();

  // 소셜로그인을 했던 유저라면
  provider && localStorage.setItem("provider", provider);

  window.location.replace('/login');
}

export default logOut;
