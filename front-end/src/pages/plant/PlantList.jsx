import PlantTable from "./plantDetail/PlantTable";
import PlantListTag from "./PlantListTag";
import {useEffect, useState} from "react";
import AddPlant from "./AddPlant";
import getData from "../../api/backend-api/common/getData";
import {useDispatch} from "react-redux";
import {Button, Card, Col, Space} from "antd";

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

  return isAddFormOpened ? (
    <AddPlant
      afterAdd={switchAddForm}
    />
  ) : (
    <div className="justify-content-center" style={{padding: "1rem", minWidth: "100%"}}>
      <Col md="auto" style={{minWidth: "100%"}}>
        <Card sm={6}>
          <div>
            <h4 className={"text-garden"}>{"나의 식물"}</h4>
            <PlantListTag/>
          </div>
          <div className="float-end" style={{marginTop: "1rem"}}>
            <Space>
              <Button
                style={{marginBottom: "1rem"}}
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
          <PlantTable/>
        </Card>
      </Col>
    </div>
  )
}

export default PlantList;
