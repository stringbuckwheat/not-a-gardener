import {Button, ConfigProvider, Dropdown, Popconfirm, Space} from "antd";
import WateringForm from "./watering/WateringForm";
import deleteData from "../../../api/backend-api/common/deleteData";
import {useDispatch, useSelector} from "react-redux";
import GButton from "../../../components/button/GButton";
import React, {useState} from "react";
import themeGreen from "../../../theme/themeGreen";
import RepotModal from "./watering/RepotModal";
import HeavyDrinkerModal from "./watering/HeavyDrinkerModal";
import StatusModal from "./watering/StatusModal";
import {useParams} from "react-router-dom";


const WateringStatusUpdate = ({wateringCallBack}) => {
  const plantId = useParams().plantId;
  const [openRepotModal, setOpenRepotModal] = useState(false);
  const [openStatusModal, setOpenStatusModal] = useState(false);

  const dispatch = useDispatch();

  const deleteAllWatering = async () => {
    await deleteData(`/plant/${plantId}/watering`);
    dispatch({type: "setWateringsForPlantDetail", payload: res});
    dispatch({type: 'deleteAllWaterings', payload: null})
  }

  const onClickWateringFormBtn = () => {
    dispatch({type: "setWateringFormOpen", payload: true});
    dispatch({type: "setEditingKey", payload: ""});
  }

  const hideModal = () => {
    dispatch({type: "setHeavyDrinkerCheck", payload: false});
    setOpenRepotModal(false);
    setOpenStatusModal(false);
  };

  // dropdown
  const items = [
    {
      key: '1',
      label: (
        <div style={{fontWeight: "bold", width: "100%"}} onClick={() => setOpenRepotModal(true)}>분갈이</div>
      ),
    },
    {
      key: '2',
      label: (
        <div className={"width-full"} onClick={() => setOpenStatusModal(true)}>식물 상태 설정</div>
      ),
    },
    {
      key: '4',
      label: (
        <Popconfirm
          placement="topRight"
          title="물주기 기록을 모두 지웁니다"
          description="삭제한 물주기 기록은 복구할 수 없어요"
          onConfirm={deleteAllWatering}
          okText="지울래요"
          cancelText="안 지울래요"
        >
          <div>물주기 기록 전체 삭제</div>
        </Popconfirm>
      ),
    }
  ];

  const wateringFormOpened = useSelector(state => state.plantDetail.etc.wateringFormOpen);

  return (
    <ConfigProvider theme={themeGreen}>
      <RepotModal open={openRepotModal} hideModal={hideModal}/>
      <HeavyDrinkerModal hideModal={hideModal}/>
      <StatusModal open={openStatusModal} hideModal={hideModal}/>

      {wateringFormOpened
        ?
        <WateringForm wateringCallBack={wateringCallBack}/>
        :
        <Space className="float-end" style={{marginBottom: "1rem"}}>
          <Space>
            <Dropdown menu={{items,}} placement="bottomLeft">
              <Button>식물 상태 변경</Button>
            </Dropdown>
            <GButton color="teal" onClick={onClickWateringFormBtn} type={"primary"}>물주기</GButton>
          </Space>
        </Space>
      }
    </ConfigProvider>
  )
}

export default WateringStatusUpdate;
