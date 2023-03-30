import axios from "axios";

const getLogin = async (login) => {
  const res = await axios.post("/", login);
  const member = res.data;

  // local storage에 토큰과 기본 정보 저장
  localStorage.setItem("login", member.token);
  localStorage.setItem("memberNo", member.memberNo);
  localStorage.setItem("name", member.name);
  localStorage.setItem("provider", member.provider); // 가장 최근 소셜로그인을 띄울 용도
}

export default getLogin;
