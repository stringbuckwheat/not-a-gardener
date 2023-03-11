import { Space, Button, Select } from 'antd';
import getPlaceList from 'src/utils/function/getPlaceList';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import modifyPlantPlace from 'src/api/backend-api/place/modifyPlantPlace';
import getPlaceListForOptionExceptHere from 'src/api/service/getPlaceListForOptionExceptHere';

const ModifyPlantPlaceButton = (props) => {
  // 장소 번호
  const thisPlaceNo = useParams().placeNo;
  console.log("thisPlaceNo", thisPlaceNo);

  // select로 쓸 유저의 장소 리스트
  const [placeList, setPlaceList] = useState([{
    key: 0,
    value: ""
  }]);

  // 장소 리스트 받아오는 함수
  // useEffect 내부에서 사용
  const getPlaceListForOption = async () => {
    const data = await getPlaceList()

    // 이 장소가 아닌 장소만 select에 추가
    const optionPlaceList = getPlaceListForOptionExceptHere(data, thisPlaceNo);

    setPlaceList(optionPlaceList);
  }

  // 최초 렌더링 시 장소 리스트 받아오기
  useEffect(() => {
    getPlaceListForOption();
  }, [])

  // 장소 수정 함수
  const navigate = useNavigate();

  const onClick = () => {
    console.log("props.selectedPlantNo", props.selectedPlantNo);

    const data = {
      placeNo: placeNo,
      plantList: props.selectedPlantNo
    }

    modifyPlantPlace(data);
    navigate(`/place`, { replace: true });
  }

  // 변경할 장소의 placeNo 받아오기
  const [placeNo, setPlaceNo] = useState();

  const handleSelect = (value) => {
    setPlaceNo(value);
  }

  return (
    <Space size="small">
      {`${props.selectedPlantNo.length}개의 식물을`}
      <Select onChange={handleSelect} defaultValue={placeList[0].value} options={placeList} style={{ width: 100, }} />
      으로
      <Button onClick={onClick} type="primary" ghost>이동</Button>
    </Space>
  )
}

export default ModifyPlantPlaceButton;
