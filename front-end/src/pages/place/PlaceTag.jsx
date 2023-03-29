import {Space, Tag} from 'antd';

const PlaceTag = (props) => {
  const place = props.place;
  const howManyPlant = props.howManyPlant;

  let artificialLightTag = "식물등을 사용하지 않아요";

  if (place.artificialLight === "사용") {
    artificialLightTag = "식물등"
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
