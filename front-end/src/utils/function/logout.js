const LogOut = () => {
  const provider = localStorage.getItem("provider");
  console.log("로그아웃 시의 provider", provider);

  localStorage.clear();

  // 소셜로그인을 했던 유저라면
  if(provider){
    localStorage.setItem("provider", provider);
  }

  window.location.replace('/');
}

export default LogOut;
