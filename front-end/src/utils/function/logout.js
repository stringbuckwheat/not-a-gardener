const logOut = () => {
  const provider = localStorage.getItem("provider");
  localStorage.clear();

  // 소셜로그인을 했던 유저라면
  provider && localStorage.setItem("provider", provider);

  window.location.replace('/login');
}

export default logOut;
