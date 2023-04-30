/**
 * local storage에 토큰과 기본 정보 저장
 * @param gardener
 * @returns {Promise<void>}
 */
const setGardener = async (gardener) => {
  localStorage.setItem("login", gardener.token);
  localStorage.setItem("gardenerId", gardener.gardenerId);
  localStorage.setItem("name", gardener.name);
  localStorage.setItem("provider", gardener.provider); // 가장 최근 소셜로그인을 띄울 용도
}

export default setGardener;
