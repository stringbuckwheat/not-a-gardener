const verifyEmail = (email) => {
  const emailRegex = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
  return emailRegex.test(email);
}

export default verifyEmail;
