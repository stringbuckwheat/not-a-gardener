import PlantTable from "./plantDetail/PlantTable";
import PlantListTag from "./PlantListTag";
import {useEffect, useState} from "react";
import AddPlant from "./AddPlant";
import getData from "../../api/backend-api/common/getData";
import {useDispatch} from "react-redux";
import {Button, Card, Col, Input, Space} from "antd";
import {SearchOutlined} from "@ant-design/icons";

/**
 * 식물 리스트 메인 페이지
 * @param plantList
 * @param setPlantList
 * @param originPlantList
 * @param addPlant
 * @returns {JSX.Element} 식물 추가 페이지, 식물 리스트 페이지
 * @constructor
 */
const PlantList = () => {
  // const [searchWord, setSearchWord] = useState("");
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);

  const dispatch = useDispatch();

  const onMountPlantList = async () => {
    const rawPlace = await getData("/place");
    dispatch({type: "setPlaces", payload: rawPlace});
  }

  useEffect(() => {
    onMountPlantList();
  }, []);

  const switchAddForm = () => setIsAddFormOpened(!isAddFormOpened);

  const [search, setSearch] = useState();
  const onChangeSearch = (e) => {
    console.log("검색어!", e.target.value);
    setSearch(e.target.value);
  }

  return isAddFormOpened ? (
    <AddPlant afterAdd={switchAddForm}/>
  ) : (
    <div className="justify-content-center" style={{padding: "1rem", minWidth: "100%"}}>
      <Col md="auto" style={{minWidth: "100%"}}>
        <Card sm={6}>
          <div>
            <h4 className={"text-garden"} style={{marginBottom: "0.5rem"}}>{"나의 식물"}</h4>
            <PlantListTag/>
          </div>
          <div className="float-end" style={{marginTop: "1rem"}}>
            <Space style={{marginBottom: "1rem"}}>
              <Input onChange={onChangeSearch} prefix={<SearchOutlined />} style={{width: "8rem"}} />
              <Button
                onClick={switchAddForm}
                type={"default"}
                color="success"
                size="sm"
                variant="outline"
                shape="rounded-pill">
                식물 추가하기
              </Button>
            </Space>
          </div>
          <PlantTable search={search}/>
        </Card>
      </Col>
    </div>
  )
}

export default PlantList;
