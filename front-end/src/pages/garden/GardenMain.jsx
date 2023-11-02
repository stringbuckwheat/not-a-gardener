import GardenTodoList from "./todolist/GardenTodoList";
import React, {useEffect} from "react";
import {CRow} from "@coreui/react";
import GButton from "../../components/button/GButton";
import WaitingForWateringList from "./waitinglist/WaitingForWateringList";
import RoutineList from "./RoutineList";
import {notification, Space} from "antd";
import Empty from "../../components/empty/Empty";
import CIcon from "@coreui/icons-react";
import {cilHappy} from "@coreui/icons";
import Booped from "../../components/animation/Booped";
import {Link} from "react-router-dom";
import getData from "../../api/backend-api/common/getData";
import {useDispatch, useSelector} from "react-redux";

const GardenMain = ({
                      todoList,
                      updateGardenAfterWatering,
                      waitingList,
                      routineList,
                      afterRoutine,
                      postponeWatering,
                      deleteInWaitingListAndTodoList
                    }) => {

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
        <div className="text-garden bold fs-4">가드너 <span className="bg-york">{useSelector(state => state.sidebar.name)}</span>님의 오늘
          할 일이에요!
        </div>
        <Booped rotation={20} timing={200}>
          <CIcon icon={cilHappy} height={30} className="text-success"/>
        </Booped>
      </Space>
      <CRow className="mt-4">
        <RoutineList routineList={routineList} afterRoutine={afterRoutine}/>
        {
          waitingList.length == 0
            ? <></>
            : <WaitingForWateringList
              waitingList={waitingList}
              openNotification={openNotification}
              deleteInWaitingListAndTodoList={deleteInWaitingListAndTodoList}
            />
        }
      </CRow>
      {
        todoList.length == 0
          ?
          <Empty title="목마른 식물이 없어요"/>
          :
          <>
            <Link to={"/plant"} className="d-flex justify-content-end mb-4 mt-3">
              <GButton color="teal" className="float-end">전체 식물 보기</GButton>
            </Link>
            <div className="mt-2">
              <GardenTodoList
                postponeWatering={postponeWatering}
                updateGardenAfterWatering={updateGardenAfterWatering}
                openNotification={openNotification}
                deleteInWaitingListAndTodoList={deleteInWaitingListAndTodoList}
                todoList={todoList}/>
            </div>
          </>
      }
    </div>
  )
}

export default GardenMain
