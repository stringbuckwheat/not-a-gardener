import {Button, Popconfirm, Space} from "antd";
import WateringForm from "./WateringForm";
import deleteData from "../../../../api/backend-api/common/deleteData";
import {useDispatch} from "react-redux";

const HandleWateringForm = ({
                              plantId,
                              setWateringList,
                              chemicalList,
                              isWateringFormOpen,
                              setIsWateringFormOpen,
                              setEditingKey,
                              wateringCallBack,
                              page
                            }) => {
  const dispatch = useDispatch();

  const deleteAllWatering = async () => {
    await deleteData(`/plant/${plantId}/watering`);
    setWateringList([]);
    dispatch({type: 'deleteAllWaterings', payload: null})
  }

  const onClickWateringFormBtn = () => {
    setIsWateringFormOpen(!isWateringFormOpen)
    setEditingKey('');
  }

  return (
    <>
      {isWateringFormOpen
        ?
        <WateringForm
          plantId={plantId}
          closeForm={() => setIsWateringFormOpen(!isWateringFormOpen)}
          chemicalList={chemicalList}
          wateringCallBack={wateringCallBack}
          page={page}
        />
        :
        <Space className="float-end" style={{marginBottom: "1rem"}}>
          <Popconfirm
            placement="topRight"
            title="물주기 기록을 모두 지웁니다"
            description="삭제한 물주기 기록은 복구할 수 없어요"
            onConfirm={deleteAllWatering}
            okText="네"
            cancelText="아니요"
          >
            <Button type={"text"}>물주기 기록 전체 삭제</Button>
          </Popconfirm>
          <Button onClick={onClickWateringFormBtn} type="primary">물주기</Button>
        </Space>
      }
    </>
  )
}

export default HandleWateringForm;
