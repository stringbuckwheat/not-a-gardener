import getData from "../../../../api/backend-api/common/getData";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {Alert, Button, Popconfirm, Space, Tag} from "antd";
import PlantStatusCode from "../../../../utils/code/plantStatusCode";
import {CloseOutlined} from "@ant-design/icons";
import "./PlantStatus.scss"
import deleteData from "../../../../api/backend-api/common/deleteData";
import {useDispatch} from "react-redux";

const PlantStatus = () => {
  const dispatch = useDispatch();
  const plantId = useParams().plantId;

  const [status, setStatus] = useState([]);

  const onMountStatus = async () => {
    const res = await getData(`/plant/${plantId}/status`);
    setStatus(res);
  }

  useEffect(() => {
    onMountStatus();
  }, []);

  const onClickDelete = async (statusId) => {
    const res = await deleteData(`/plant/${plantId}/status/${statusId}`);
    console.log("res", res);

    dispatch({type: "", payload: res.data}); // 유효한 상태 정보 redux에 수정
    setStatus(() => status.filter((s) => s.statusId != statusId));
  }

  const getMessage = (status) => {
    const active = status.active == "Y" ? "등록" : "해제";

    return (
      <Space className="space-container">
        <div className="content-container">
          <Tag className="tag">{status.recordedDate}</Tag>
          <span className="status-text">{`${PlantStatusCode[status.status].name} ${active}`}</span>
        </div>
        <Popconfirm
          title="이 상태 정보를 삭제할까요?"
          okText="네"
          cancelText="아니요"
          onConfirm={() => onClickDelete(status.statusId)}>
          <CloseOutlined className="close-icon"/>
        </Popconfirm>
      </Space>
    )
  }

  return (
    <Space direction="vertical" className={"width-full"}>
      {
        status?.map((status) =>
          <Alert
            key={status.statusId}
            message={getMessage(status)}
            type={status.active === "N" ? "success" : PlantStatusCode[status.status].type}
          />
        )
      }
    </Space>
  )
}

export default PlantStatus
