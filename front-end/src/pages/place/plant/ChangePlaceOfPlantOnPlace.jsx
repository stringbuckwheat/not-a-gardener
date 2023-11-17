import {Space, Select} from 'antd';
import getPlaceList from 'src/api/service/getPlaceList';
import {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import getPlaceListForOptionExceptHere from 'src/api/service/getPlaceListForOptionExceptHere';
import {SwapRightOutlined} from '@ant-design/icons';
import ValidationSubmitButton from "../../../components/button/ValidationSubmitButton";
import updateData from "../../../api/backend-api/common/updateData";

/**
 * 장소 페이지에서 (이미 이 장소에 있는) 식물 다른 장소로 변경하기
 * 체크박스 눌러서 선택한 식물 다른 장소로 보냄
 * @param setSelectedRowKeys
 * @param selectedPlantId
 * @returns {JSX.Element}
 * @constructor
 */
const ChangePlaceOfPlantOnPlace = ({setSelectedRowKeys, selectedPlantId}) => {
  // 이 장소의 번호
  const thisPlaceId = useParams().placeId;

  // 장소 수정 함수
  const navigate = useNavigate();

  // 변경할 장소의 placeId
  const [placeId, setPlaceId] = useState(0);

  // select로 쓸 유저의 장소 리스트
  const [placeList, setPlaceList] = useState([{
    label: "",
    value: 0
  }]);

  // 장소 리스트 받아오는 함수
  // useEffect 내부에서 사용
  const getPlaceListForOption = async () => {
    const data = await getPlaceList();

    // 이 장소가 아닌 장소만 select에 추가
    const optionPlaceList = getPlaceListForOptionExceptHere(data, thisPlaceId);
    setPlaceList(optionPlaceList);
  }

  // 최초 렌더링 시 장소 리스트 받아오기
  useEffect(() => {
    getPlaceListForOption();
  }, [])


  const modifyPlaceOfPlant = async () => {
    await updateData(`/plant/place/${placeId}`, {placeId, plants: selectedPlantId})
    setSelectedRowKeys([]);
    navigate(`/place/${placeId}`, {state: {placeId: placeId}});
  }

  return (
    <div style={{marginBottom: "1.5rem", float: "right"}}>
      <Space>
        <SwapRightOutlined
          style={{fontSize: "1.5rem", color: "green"}}/>
        {`${selectedPlantId.length}개의 식물을`}
        <Select
          size={"small"}
          style={{width: "8rem", height: "1.7rem"}}
          onChange={(value) => setPlaceId(value)}
          options={placeList}/>
        으로
        <ValidationSubmitButton
          size={"small"}
          isValid={placeId !== 0}
          onClickInvalidMsg={"장소는 비워둘 수 없어요"}
          title={"이동"}
          onClickValid={modifyPlaceOfPlant}/>
      </Space>
    </div>
  )
}

export default ChangePlaceOfPlantOnPlace;
