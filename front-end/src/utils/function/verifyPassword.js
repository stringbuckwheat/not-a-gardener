const verifyPassword = (password) => {
  // 비밀번호: 숫자, 특문 각 1회 이상, 영문은 2개 이상 사용하여 8자리 이상 입력
  const pwRegex = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/;

  return pwRegex.test(password);
}

export default verifyPassword;
