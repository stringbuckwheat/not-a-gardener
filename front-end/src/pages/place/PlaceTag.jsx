import {Space, Tag} from 'antd';

/**
 * 장소 상세페이지 내 정보 태그
 * @param place
 * @param howManyPlant
 * @returns {JSX.Element}
 * @constructor
 */
const PlaceTag = ({place, howManyPlant}) => {

  let artificialLightTag = "식물등을 사용하지 않아요";

  if (place.artificialLight === "사용") {
    artificialLightTag = "식물등을 사용해요"
  }

  return (
    <Space size={[0, 8]} wrap>
      <Tag color="blue">{place.option}</Tag>
      <Tag color="green">{howManyPlant}개의 식물</Tag>
      <Tag color="gold">{artificialLightTag}</Tag>
    </Space>
  )
}

export default PlaceTag;
