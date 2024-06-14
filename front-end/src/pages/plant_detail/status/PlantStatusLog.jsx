import getData from "../../../api/backend-api/common/getData";
import {useParams} from "react-router-dom";
import {useEffect} from "react";
import {Alert, Popconfirm, Space, Tag} from "antd";
import PlantStatusCode from "../../../utils/code/plantStatusCode";
import {CloseOutlined} from "@ant-design/icons";
import "./Status.scss"
import deleteData from "../../../api/backend-api/common/deleteData";
import {useDispatch, useSelector} from "react-redux";
import PlantDetailAction from "../../../redux/reducer/plant_detail/plantDetailAction";

const PlantStatusLog = () => {
  const statusLog = useSelector(state => state.plantDetail.statusLog);
  const dispatch = useDispatch();
  const plantId = useParams().plantId;

  const onMountStatus = async () => {
    const res = await getData(`/plant/${plantId}/status/log`);
    console.log("res", res);
    dispatch({type: PlantDetailAction.FETCH_STATUS_LOG, payload: res});
  }

  useEffect(() => {
    onMountStatus();
  }, []);

  const onClickDelete = async (statusLogId) => {
    const res = await deleteData(`/plant/${plantId}/status/log/${statusLogId}`);
    console.log("res", res);

    dispatch({type: PlantDetailAction.FETCH_ACTIVE_STATUS, payload: res.data}); // 유효한 상태 정보 redux에 수정
    dispatch({type: PlantDetailAction.DELETE_STATUS_LOG_ONE, payload: statusLogId}); // 로그에서 삭제
  }

  const getMessage = (status) => {
    console.log("getMessage status", status);
    const active = status.active == "Y" ? "등록" : "해제";

    return (
      <Space className="space-container">
        <div className="content-container">
          <Tag className="tag">{status.recordedDate}</Tag>
          <span className="status-text">{`${PlantStatusCode[status.statusType]?.name} ${active}`}</span>
        </div>
        <Popconfirm
          title="이 상태 정보를 삭제할까요?"
          okText="네"
          cancelText="아니요"
          onConfirm={() => onClickDelete(status.statusLogId)}>
          <CloseOutlined className="close-icon"/>
        </Popconfirm>
      </Space>
    )
  }

  return statusLog.length == 0 ? (
    <Alert
      message={"식물 상태 기록이 없어요."}
      type={"warning"}
    />
  ) : (
    <Space direction="vertical" className={"width-full"}>
      {
        statusLog?.map((status) =>
          <Alert
            key={status.statusLogId}
            message={getMessage(status)}
            type={status.active === "N" ? "success" : PlantStatusCode[status.statusType].type}
          />
        )
      }
    </Space>
  )
}

export default PlantStatusLog
