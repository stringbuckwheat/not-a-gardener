/**
 * local storage에 토큰과 기본 정보 저장
 * @param gardener
 * @returns {Promise<void>}
 */
const setGardener = async (res) => {
  console.log("login 이후 gardener response", res);

  const info = res.simpleInfo;
  const token = res.token;

  // token
  localStorage.setItem("accessToken", token.accessToken);
  localStorage.setItem("refreshToken", token.refreshToken);
  localStorage.setItem("expiredAt", token.expiredAt);

  // 기본 정보
  localStorage.setItem("gardenerId", info.gardenerId);
  localStorage.setItem("name", info.name);
  localStorage.setItem("provider", info.provider); // 가장 최근 소셜로그인을 띄울 용도
}

export default setGardener;
