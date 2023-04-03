
const getWateringMsg = (gardenDetail) => {
  let wateringMsg;

  if (gardenDetail.wateringCode < 0) {
    // 물주기 놓침
    wateringMsg = `물 주기를 ${gardenDetail.wateringDDay * -1}일 놓쳤어요!\n비료를 주지 마세요`
  } else if (gardenDetail.wateringCode == 0) {
    // 물주기 정보 부족
    wateringMsg = "물주기 정보가 부족해요!"
  } else if (gardenDetail.wateringCode == 1) {
    // 물을 줘야함
    if (gardenDetail.fertilizingCode != -1) { // -1 == 유저가 비료 하나도 없음
      wateringMsg = `오늘은 물 주는 날이에요. \n${gardenDetail.fertilizingCode}번 비료를 주세요!`
    } else {
      wateringMsg = `오늘은 물 주는 날이에요. \n맹물을 주세요!`
    }
  } else if (gardenDetail.wateringCode == 2) {
    // 물 주기 하루 전(목마를 확률 높음)
    wateringMsg = "물 주기가 하루 남았어요. \n화분이 말랐는지 체크해보세요!\n말랐다면"

    if (gardenDetail.fertilizingCode != -1) {
      wateringMsg += ` ${gardenDetail.fertilizingCode}번 비료를 주세요!`
    } else {
      wateringMsg += ` 맹물을 주세요!`
    }

  } else if (gardenDetail.wateringCode == 3) {
    wateringMsg = "이 식물은 지금 행복해요. 가만히 두세요."
  } else if (gardenDetail.wateringCode == 4) {
    wateringMsg = "이 식물은 오늘 물을 마셨어요!"
  }

  return wateringMsg;
}

export default getWateringMsg;
