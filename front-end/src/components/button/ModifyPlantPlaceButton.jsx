import { Space, Button, Select } from 'antd';
import authAxios from 'src/utils/interceptors';
import getPlaceList from 'src/utils/function/getPlaceList';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const ModifyPlantPlaceButton = (props) => {
  const thisPlaceNo = useParams().placeNo;
  console.log("thisPlaceNo", thisPlaceNo);

  const [placeList, setPlaceList] = useState([{
    key: 0,
    value: ""
  }]);

  const getPlaceListForOption = async () => {
    const data = await getPlaceList()

    const optionPlaceList = data.filter((place) => place.key != thisPlaceNo)
    .map((place) => {
        return (
          {
            label: place.value,
            value: place.key
          }
        )
      })

    setPlaceList(optionPlaceList);
  }

  useEffect(() => {
    getPlaceListForOption();
  }, [])

  const navigate = useNavigate();

    const onClick = () => {
      console.log("props.selectedPlantNo", props.selectedPlantNo);
      const data = {
        placeNo: placeNo,
        plantList: props.selectedPlantNo
      }

      console.log("data", data);

      authAxios.put("/plant/modify-place", data)
      .then(
        navigate("/place")
      )
    }

    const [ placeNo, setPlaceNo ] = useState();

    const handleSelect = (value) => {
      setPlaceNo(value);
    }

    return (
      <Space size="small">
        {`${props.selectedPlantNo.length}개의 식물을`}
        <Select onChange={handleSelect} defaultValue={placeList[0].value} options={placeList} style={{ width: 100,}}/>
        으로
        <Button onClick={onClick} type="primary" ghost>이동</Button>
      </Space>
    )
  }

  export default ModifyPlantPlaceButton;