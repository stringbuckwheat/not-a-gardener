import AfterWateringCode from "../code/afterWateringCode";

const getAfterWateringMsg = (afterWateringCode) => {
  let title = "";
  let content = "";

  if (afterWateringCode == AfterWateringCode.SCHEDULE_SHORTEN) {
    title = "물주기가 줄어들었어요!"
    content = "물주기가 줄어든 이유는 다음과 같습니다.\n" +
      "1) 식물이 성장함\n" +
      "2) 볕이 잘 드는 장소로 옮김\n" +
      "3) 날씨가 더워졌거나 줄곧 맑았음\n" +
      "4) 식물이 있는 장소의 습도가 낮아짐"
  } else if (afterWateringCode == AfterWateringCode.NO_CHANGE) {
    title = "물주기 계산에 변동이 없습니다."
    content = ""
  } else if (afterWateringCode == AfterWateringCode.SCHEDULE_LENGTHEN) {
    title = "물주기가 늘어났어요"
    content = "물주기가 늘어난 이유는 다음과 같습니다.\n" +
      "1) 식물에 문제가 생겼을 수 있어요. 점검해보세요.\n" +
      "2) 볕이 덜 드는 장소로 옮김\n" +
      "3) 날씨가 추워졌거나 줄곧 흐렸음\n" +
      "4) 식물이 있는 장소의 습도가 높아짐"
  } else if (afterWateringCode == AfterWateringCode.FIRST_WATERING) {
    title = "축하합니다! 처음으로 물주기를 기록하셨네요"
    content = "관수 주기를 기록하며 함께 키워요"
  } else if (afterWateringCode == AfterWateringCode.SECOND_WATERING) {
    title = "두 번째 물주기를 기록했어요"
    content = "한 번 더 관수하면 물주기 관찰이 완료됩니다"
  } else if (afterWateringCode == AfterWateringCode.INIT_WATERING_PERIOD) {
    title = "첫 물 주기 간격을 기록했어요!"
    content = "관찰 결과에 따라 자동으로 업데이트 됩니다"
  }

  return {
    title: title,
    content: content
  }
}

export default getAfterWateringMsg;
