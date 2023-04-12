import {Space, Select} from 'antd';
import getPlaceList from 'src/api/service/getPlaceList';
import {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import modifyPlantPlace from 'src/api/backend-api/place/modifyPlantPlace';
import getPlaceListForOptionExceptHere from 'src/api/service/getPlaceListForOptionExceptHere';
import {SwapRightOutlined} from '@ant-design/icons';
import ValidationSubmitButton from "../../../components/button/ValidationSubmitButton";

/**
 * 장소 페이지에서 (이미 이 장소에 있는) 식물 다른 장소로 변경하기
 * 체크박스 눌러서 선택한 식물 다른 장소로 보냄
 * @param setSelectedRowKeys
 * @param selectedPlantNo
 * @returns {JSX.Element}
 * @constructor
 */
const ChangePlaceOfPlantOnPlace = ({setSelectedRowKeys, selectedPlantNo}) => {

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


  const modifyPlaceOfPlant = async () => {
    await modifyPlantPlace({placeNo: placeNo, plantList: selectedPlantNo});
    setSelectedRowKeys([]);
    navigate(`/place/${placeNo}`);
  }

  return (
    <div className="mb-3 float-end">
      <Space>
        <SwapRightOutlined
          className="font-size-20 text-success"/>
        {`${selectedPlantNo.length}개의 식물을`}
        <Select
          size={"small"}
          className="width-100"
          onChange={(value) => setPlaceNo(value)}
          options={placeList}/>
        으로
        <ValidationSubmitButton
          size={"small"}
          isValid={placeNo !== 0}
          onClickInvalidMsg={"장소는 비워둘 수 없어요"}
          title={"이동"}
          onClickValid={modifyPlaceOfPlant}/>
      </Space>
    </div>
  )
}

export default ChangePlaceOfPlantOnPlace;
