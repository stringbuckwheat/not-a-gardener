import axios from "axios";

const getLogin = async (login) => {
  const res = await axios.post("/", login);
  const member = res.data;

  // local storage에 토큰과 기본 정보 저장
  localStorage.setItem("login", member.token);
  localStorage.setItem("memberNo", member.memberNo);
  localStorage.setItem("name", member.name);
}

export default getLogin;
