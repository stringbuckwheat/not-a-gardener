const isEndWithVowel = (title) => {
  const finalCharCode = title.charCodeAt(title.length - 1);
  // console.log("finalCharCode", finalCharCode);

  if (finalCharCode >= 48 && finalCharCode <= 57) { // 숫자
    return isNumberEndWithVowel(finalCharCode); // 조사 구하기
  } else if (finalCharCode >= 44032 && finalCharCode <= 55203) { // 한글
    return isKoreanEndWithVowel(finalCharCode)
  } else if (
    (finalCharCode >= 65 && finalCharCode <= 90) // 영문 대문자
    || (finalCharCode >= 97 && finalCharCode <= 122) // 소문자
  ) {
    return isEnglishEndWithVowel(finalCharCode)
  } else {
    // 숫자, 한글, 영어에 해당하지 않으면 뒤에서 두 번째거로
    // isEndWithVowel(title.substring(title.length - 2));
  }
}

const isNumberEndWithVowel = (finalCharCode) => {
  const number = finalCharCode - 48;
  return (number == 2 || number == 3 || number == 4 || number == 5 || number == 9)
}

// 은/는, 을/를 구분 함수...
const isKoreanEndWithVowel = (finalCharCode) => {
  // 0이면 받침 없고 아니면 받침 있음
  const tmp = (finalCharCode - 44032) % 28;
  return tmp == 0;
}

const isEnglishEndWithVowel = (finalCharCode) => {
  const finalCharacter = String.fromCharCode(finalCharCode).toLowerCase();
  return (finalCharacter == "a" || finalCharacter == "e" || finalCharacter == "i" || finalCharacter == "o" || finalCharacter == "u" || finalCharacter == "y")
}

export default isEndWithVowel;
