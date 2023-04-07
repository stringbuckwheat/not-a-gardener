import {
  cilDrop, cilFace,
  cilHandPointUp,
  cilHappy,
  cilMeh,
  cilMoodGood,
  cilZoom
} from "@coreui/icons";


const wateringCodeDesign = [
  {
    // 0    물주기 정보 부족
    color: "primary",
    icon: cilZoom
  },
  {
    // 1    물주기
    color: "primary",
    icon: cilDrop
  },
  {
    // 2    체크하기
    color: "warning",
    icon: cilHandPointUp
  },
  {
    // 3    물주기 늘어나는 중
    color: "success",
    icon: cilFace,
  },
  {
    // 4    놔두세요
    color: "success",
    icon: cilHappy
  },
  {
    // 5    오늘 물 줌
    color: "info",
    icon: cilMoodGood
  },
  {
    // 6    오늘 미룸
    color: "dark",
    icon: cilMeh
  }
]

export default wateringCodeDesign;
