import {Badge, Space} from "antd";
import LinkHoverTag from "../../components/tag/basic/LinkHoverTag";
import isEndWithVowel from "../../utils/function/isEndWithVowel";

const WateringList = ({wateringDetail}) => {
  if (!wateringDetail || wateringDetail.length == 0) {
    return;
  }

  const getChemicalMsg = (chemicalName) => {
    if (!chemicalName) {
      return "맹물을 주었어요"
    }

    return `${chemicalName}${isEndWithVowel(chemicalName) ? "를" : "을"} 주었어요`
  }

  return (
    <>
      {
        wateringDetail.map((watering) => (
            <div key={watering.wateringNo}>
              <Space>
                <Badge status="warning"/>
                <span>
                  <LinkHoverTag color="green" to={`/plant/${watering.plantNo}`} content={watering.plantName}/>
                  <span>{`에게 ${getChemicalMsg(watering.chemicalName)}`}</span>
                </span>
              </Space>
            </div>
          )
        )
      }
    </>
  )
}

export default WateringList;
