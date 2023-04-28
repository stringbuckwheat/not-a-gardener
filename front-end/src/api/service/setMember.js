/**
 * local storage에 토큰과 기본 정보 저장
 * @param member
 * @returns {Promise<void>}
 */
const setMember = async (member) => {
  localStorage.setItem("login", member.token);
  localStorage.setItem("memberNo", member.memberId);
  localStorage.setItem("name", member.name);
  localStorage.setItem("provider", member.provider); // 가장 최근 소셜로그인을 띄울 용도
}

export default setMember;
