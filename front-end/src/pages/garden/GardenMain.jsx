import GardenTodoList from "./todolist/GardenTodoList";
import React, {useEffect} from "react";
import WaitingForWateringList from "./WaitingForWateringList";
import RoutineList from "./RoutineList";
import {notification, Row, Space} from "antd";
import Booped from "../../components/animation/Booped";
import getData from "../../api/backend-api/common/getData";
import {useDispatch, useSelector} from "react-redux";
import {SmileOutlined} from "@ant-design/icons";


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

  // todo booped 고치기

  return (
    <div style={{marginTop: "1.5rem"}}>
      {contextHolder}
      <Space>
        <div className="text-garden" style={{fontSize: "1.7rem"}}>가드너 <span
          className="bg-york">{useSelector(state => state.sidebar.name)}</span>님의 오늘
          할 일이에요!
        </div>
        <Booped rotation={20} timing={200}>
          <SmileOutlined style={{fontSize: "2rem", color: "green"}}/>
        </Booped>
      </Space>
      <Row style={{marginTop: "1rem"}}>
        <RoutineList/>
        <WaitingForWateringList/>
      </Row>
      <GardenTodoList openNotification={openNotification}/>
    </div>
  )
}

export default GardenMain
