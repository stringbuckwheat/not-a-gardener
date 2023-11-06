import GardenTodoList from "./todolist/GardenTodoList";
import React, {useEffect} from "react";
import {CRow} from "@coreui/react";
import WaitingForWateringList from "./waitinglist/WaitingForWateringList";
import RoutineList from "./RoutineList";
import {notification, Space} from "antd";
import CIcon from "@coreui/icons-react";
import {cilHappy} from "@coreui/icons";
import Booped from "../../components/animation/Booped";
import getData from "../../api/backend-api/common/getData";
import {useDispatch, useSelector} from "react-redux";

const GardenMain = () => {
  const dispatch = useDispatch();

  const setChemicalList = async () => {
    const data = await getData("/chemical");
    dispatch({type: 'setChemicals', payload: data});
  }

  useEffect(() => {
    setChemicalList();
  }, [])

  // 식물 상태 업데이트 이후 메시지 띄우기
  const [api, contextHolder] = notification.useNotification();

  const openNotification = (msg) => {
    api.open({
      message: msg.title,
      description: msg.content,
      duration: 4,
    });
  };

  return (
    <div className="mt-3">
      {contextHolder}
      <Space>
        <div className="text-garden bold fs-4">가드너 <span
          className="bg-york">{useSelector(state => state.sidebar.name)}</span>님의 오늘
          할 일이에요!
        </div>
        <Booped rotation={20} timing={200}>
          <CIcon icon={cilHappy} height={30} className="text-success"/>
        </Booped>
      </Space>
      <CRow className="mt-4">
        <RoutineList/>
        <WaitingForWateringList />
      </CRow>
      <GardenTodoList openNotification={openNotification}/>
    </div>
  )
}

export default GardenMain
