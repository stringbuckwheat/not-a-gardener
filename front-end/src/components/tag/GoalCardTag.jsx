import {Tag} from "antd";
import LinkHoverTag from "./basic/LinkHoverTag";

const GoalCardTag = ({isCompleted, plantId, plantName}) => {
  // 달성 못했고 식물 없음 => 아무것도 없음
  // 달성 했고 식물 없음 => 완료 태그만
  // 달성 못했고 식물 있음 => 식물태그
  // 달성 했고 식물 있음 => 완료 태그, 식물 태그

  if (!isCompleted && !plantName) {
    return <></>;
  }

  const completeTag = isCompleted ? <Tag color={"yellow-inverse"} className="text-orange">달성!</Tag> : <></>;
  const plantTag = plantName
    ? <LinkHoverTag color="green" to={`/plant/${plantId}`} content={plantName}/>
    : <></>;

  return (
    <div style={{display: "flex", justifyContent: "flex-end"}}>
      {completeTag}
      {plantTag}
    </div>
  )
}

export default GoalCardTag
