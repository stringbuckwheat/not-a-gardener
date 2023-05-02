import {Badge, Popconfirm, Space} from "antd";
import LinkHoverTag from "../../components/tag/basic/LinkHoverTag";
import isEndWithVowel from "../../utils/function/isEndWithVowel";
import {useState} from "react";
import {DeleteOutlined} from "@ant-design/icons";
import DeleteIconButton from "../../components/button/DeleteIconButton";
import deleteData from "../../api/backend-api/common/deleteData";

const WateringList = ({wateringDetail, onDelete}) => {
  console.log("wateringDetail", wateringDetail);
  if (!wateringDetail || wateringDetail.length == 0) {
    return;
  }

  const deleteWatering = async (watering) => {
    console.log("watering", watering); // wateringId, wateringDate
    console.log("wateringDetail", wateringDetail);
    await deleteData(`/watering/${watering.wateringId}`);
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
        console.log("watering", watering);
        const forDelete = {wateringId: watering.id, wateringDate: watering.wateringDate};

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
              <DeleteIconButton/>
            </Popconfirm>
          </Space>
        </span>
          </Space>
        </div>)
        }
      )}
    </>)
}

export default WateringList;
