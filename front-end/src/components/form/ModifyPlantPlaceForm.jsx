import {Space, Select} from 'antd';
import getPlaceList from 'src/utils/function/getPlaceList';
import {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import modifyPlantPlace from 'src/api/backend-api/place/modifyPlantPlace';
import getPlaceListForOptionExceptHere from 'src/api/service/getPlaceListForOptionExceptHere';
import {SwapRightOutlined} from '@ant-design/icons';
import {CButton} from '@coreui/react';
import getData from 'src/api/backend-api/common/getData'

const ModifyPlantPlaceForm = (props) => {
  const {setSelectedRowKeys, selectedPlantNo} = props;

  // 이 장소의 번호
  const thisPlaceNo = useParams().placeNo;

  // 장소 수정 함수
  const navigate = useNavigate();

  // 변경할 장소의 placeNo
  const [placeNo, setPlaceNo] = useState(0);

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
    const optionPlaceList = getPlaceListForOptionExceptHere(data, thisPlaceNo);
    setPlaceList(optionPlaceList);
  }

  // 최초 렌더링 시 장소 리스트 받아오기
  useEffect(() => {
    getPlaceListForOption();
  }, [])

  const handleSelect = (value) => {
    setPlaceNo(value);
  }

  const modifyPlaceOfPlant = async () => {
    const data = {
      placeNo: placeNo,
      plantList: selectedPlantNo
    }

    const res = await modifyPlantPlace(data);

    setSelectedRowKeys([]);
    navigate(`/place/${placeNo}`, {replace: true, state: res});
  }

  return (
    <div className="mb-3 float-end">
      <Space>
        <SwapRightOutlined
          className="font-size-20 text-success"/>
        {`${props.selectedPlantNo.length}개의 식물을`}
        <Select
          className="width-100"
          onChange={handleSelect}
          options={placeList}/>
        으로
        {
          placeNo !== 0
            ? <CButton onClick={modifyPlaceOfPlant} color="success" variant="outline" size="sm">이동</CButton>
            : <CButton disabled color="dark" variant="outline" size="sm">이동</CButton>
        }
      </Space>
    </div>
  )
}

export default ModifyPlantPlaceForm;
