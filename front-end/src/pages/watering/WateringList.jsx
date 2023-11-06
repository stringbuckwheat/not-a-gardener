import {Badge, Popconfirm, Space} from "antd";
import LinkHoverTag from "../../components/tag/basic/LinkHoverTag";
import isEndWithVowel from "../../utils/function/isEndWithVowel";
import DeleteIconBtn from "../../components/button/DeleteIconBtn";
import deleteWithData from "../../api/backend-api/common/deleteWithData";

const WateringList = ({wateringDetail, onDelete}) => {
  if (!wateringDetail || wateringDetail.length == 0) {
    return;
  }

  const deleteWatering = async (watering) => {
    console.log("delete watering", watering); // wateringId, wateringDate
    console.log("wateringDetail", wateringDetail);
    await deleteWithData(`/watering/${watering.wateringId}`, {plantId: watering.plantId});
    onDelete && onDelete(watering);
  }

  const getChemicalMsg = (chemicalName) => {
    if (!chemicalName) {
      return "맹물을 주었어요"
    }

    return `${chemicalName}${isEndWithVowel(chemicalName) ? "를" : "을"} 주었어요`
  }

  return (
    <>
      {wateringDetail.map((watering) => {
          console.log("watering in map", watering);

          const forDelete = {wateringId: watering.id, wateringDate: watering.wateringDate, plantId: watering.plantId};

          return (
            <div key={watering.id}>
              <Space>
                <Badge status="warning"/>
                <span>
                  <LinkHoverTag color="green" to={`/plant/${watering.plantId}`} content={watering.plantName}/>
                  <Space>
                  <span>{`에게 ${getChemicalMsg(watering.chemicalName)}`}</span>
                  <Popconfirm
                    title={"이 물주기를 삭제할까요?"}
                    okText="네"
                    cancelText="아니요"
                    onConfirm={() => deleteWatering(forDelete)}>
                    <DeleteIconBtn/>
                  </Popconfirm>
                </Space>
                </span>
              </Space>
            </div>
          )
        }
      )}
    </>)
}

export default WateringList;
