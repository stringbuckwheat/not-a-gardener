import {useDispatch, useSelector} from "react-redux";
import getData from "../../../api/backend-api/common/getData";
import {useParams} from "react-router-dom";
import {useEffect} from "react";
import {Alert, Popconfirm, Space, Tag} from "antd";
import PlantDetailAction from "../../../redux/reducer/plant_detail/plantDetailAction";
import {CloseOutlined} from "@ant-design/icons";
import deleteData from "../../../api/backend-api/common/deleteData";
import "../status/Status.scss"

const RepotLog = () => {
  const repot = useSelector(state => state.plantDetail.repot);

  const dispatch = useDispatch();
  const plantId = useParams().plantId;

  const onMountRepot = async () => {
    const res = await getData(`/plant/${plantId}/repot`);
    dispatch({type: PlantDetailAction.FETCH_REPOT, payload: res});
  }

  useEffect(() => {
    onMountRepot();
  }, []);

  const onClickDelete = async (repotId) => {
    console.log("delete repotId", repotId);

    await deleteData(`/repot/${repotId}`);
    dispatch({type: PlantDetailAction.DELETE_REPOT, payload: repotId});
  }

  const getMessage = (repot) => {
    console.log("repot", repot);
    const initPeriod = repot.initPeriod == "Y" ? "초기화" : "유지";

    return (
      <Space className="space-container">
        <div className="content-container">
          <Tag className="tag">{repot.repotDate}</Tag>
          <span className="status-text">{`물주기 간격 ${initPeriod}`}</span>
        </div>
        <Popconfirm
          title="이 분갈이 정보를 삭제할까요?"
          okText="네"
          cancelText="아니요"
          onConfirm={() => onClickDelete(repot.repotId)}>
          <CloseOutlined className="close-icon"/>
        </Popconfirm>
      </Space>
    )
  }

  return repot.length == 0 ? (
    <Alert
      message={"분갈이 기록이 없어요."}
      type={"warning"}
    />
  ) : (
    <Space direction="vertical" className={"width-full"}>
      {
        repot?.map((repot) =>
          <Alert
            key={repot.repotId}
            message={getMessage(repot)}
            type={"success"}
          />
        )
      }
    </Space>
  )
}

export default RepotLog;
