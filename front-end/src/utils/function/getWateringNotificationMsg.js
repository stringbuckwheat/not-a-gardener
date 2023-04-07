const getWateringNotificationMsg = (afterWateringCode) => {
  let title = "";
  let content = "";

  // -1 물주기가 줄어들었어요!
  // 0 물주기 계산에 변동이 없습니다.
  // 1 물주기가 늘어났어요
  // 2 처음으로 물주기를 기록
  // 3 두 번째 물주기 기록

  if (afterWateringCode == -1) {
    title = "물주기가 줄어들었어요!"
    content = "물주기가 줄어든 이유는 다음과 같습니다.\n" +
      "1) 식물이 성장함\n" +
      "2) 볕이 잘 드는 장소로 옮김\n" +
      "3) 날씨가 더워졌거나 줄곧 맑았음\n" +
      "4) 식물이 있는 장소의 습도가 낮아짐"
  } else if (afterWateringCode == 0) {
    title = "물주기 계산에 변동이 없습니다."
    content = ""
  } else if (afterWateringCode == 1) {
    title = "물주기가 늘어났어요"
    content = "물주기가 늘어난 이유는 다음과 같습니다.\n" +
      "1) 식물에 문제가 생겼을 수 있어요. 점검해보세요.\n" +
      "2) 볕이 덜 드는 장소로 옮김\n" +
      "3) 날씨가 추워졌거나 줄곧 흐렸음\n" +
      "4) 식물이 있는 장소의 습도가 높아짐"
  } else if (afterWateringCode == 2) {
    title = "축하합니다! 처음으로 물주기를 기록하셨네요"
    content = "관수 주기를 기록하며 함께 키워요"
  } else if (afterWateringCode == 3) {
    title = "두 번째 물주기 기록"
    content = "관수 주기를 기록하며 함께 키워요"
  }

  return {
    title: title,
    content: content
  }
}

export default getWateringNotificationMsg;
