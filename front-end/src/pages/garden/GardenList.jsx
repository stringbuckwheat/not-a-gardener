import GardenCard from "../../components/card/GardenCard";
import React, {useEffect, useState} from "react";
import {notification} from "antd";
import getWateringNotificationMsg from "../../utils/function/getWateringNotificationMsg";
import ListHeader from "../../components/data/header/ListHeader";
import AddPlantButton from "../../components/button/AddPlantButton";
import Booped from "./Booped";
import CIcon from "@coreui/icons-react";
import {cilHappy} from "@coreui/icons";
import {CContainer, CRow} from "@coreui/react";

const GardenList = (props) => {
  const {plantList, setPlantList, originPlantList} = props;

  // 식물 상태 업데이트 이후 메시지 띄우기
  const [api, contextHolder] = notification.useNotification();
  const openNotification = (wateringMsg) => {
    const msg = getWateringNotificationMsg(wateringMsg.wateringCode)

    api.open({
      message: msg.title,
      description: msg.content,
      duration: 4,
    });
  };

  // 검색
  const [searchWord, setSearchWord] = useState("");

  const search = (searchWord) => {
    const searchedList = originPlantList.filter(plant => plant.plant.plantName.includes(searchWord));
    setPlantList(searchedList);
  }

  useEffect(() => {
    console.log("현재 검색어: ", searchWord);
    if (searchWord !== "") {
      search(searchWord)
    } else {
      setPlantList(originPlantList);
    }
  }, [searchWord])


  // 정렬
  const [sort, setSort] = useState("");

  const sortOption = [
    {value: 'code', label: '할일 바쁜 순'},
    {value: 'abc', label: '가나다순'},
    {value: 'createDateDesc', label: '등록순'},
  ]

  useEffect(() => {
    const sortedPlantList = [...plantList];

    if (sort === "code") {
      sortedPlantList.sort((a, b) => (a.gardenDetail.wateringCode - b.gardenDetail.wateringCode))
    } else if (sort === "abc") {
      sortedPlantList.sort((a, b) => (a.plant.plantName < b.plant.plantName ? -1 : a.plant.plantName > b.plant.plantName ? 1 : 0))
    } else if (sort === "createDateDesc") {
      sortedPlantList.sort((a, b) => new Date(b.plant.createDate) - new Date(a.plant.createDate));
    }

    setPlantList(sortedPlantList);
  }, [sort])

  return (
    <>
      {contextHolder}
      <div>
        아이콘 애니메이션
        <Booped rotation={20} timing={200}>
          <CIcon icon={cilHappy} height={40}/>
        </Booped>
        <AddPlantButton size="sm"/>
      </div>
      <ListHeader
        setSearchWord={setSearchWord}
        sortOption={sortOption}
        setSort={setSort}/>
        <CRow>
          {plantList.map((plant) =>
            <GardenCard
              plant={plant}
              // chemicalList={chemicalList}
              setPlantList={setPlantList}
              openNotification={openNotification}
            />
          )}
        </CRow>
    </>
  )

}

export default GardenList;
