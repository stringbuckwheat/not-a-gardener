import {Popconfirm, Space, Table, Tag} from 'antd';
import {useEffect, useState} from 'react';
import ChangePlaceOfPlantOnPlace from './ChangePlaceOfPlantOnPlace';
import AddPlantInPlaceButtons from './AddPlantInPlaceButtons';
import getPlantListForPlacePlantTable from "../../../utils/function/getPlantListForPlacePlantTable";
import deleteData from "../../../api/backend-api/common/deleteData";
import getData from "../../../api/backend-api/common/getData";
import {Link, useParams} from "react-router-dom";
import {DeleteOutlined} from "@ant-design/icons";

const getPlantTableColArrInPlace = (deletePlant) => {

  return (
    [
      {
        title: '식물 이름',
        dataIndex: 'name',
        key: 'name',
        render: (_, record) =>
          (
            <Link to={`/plant/${record.id}`} className="text-decoration-none">
              {record.name}
            </Link>
          )
      },
      {
        title: '식물 종',
        dataIndex: 'species',
        key: 'species',
        responsive: ['lg']
      },
      {
        title: '최근 물주기',
        dataIndex: 'recentWateringPeriod',
        key: 'recentWateringPeriod',
        responsive: ['lg']
      },
      {
        title: '기타',
        key: 'tags',
        dataIndex: 'tags',
        responsive: ['lg'],
        render: (_, {tags}) => (
          <>
            {tags.map((tag, idx) => (<Tag key={idx}>{tag}</Tag>))}
          </>
        ),
      },
      {
        title: '',
        dataIndex: 'plantDetail',
        key: 'plantDetail',
        render: (_, record) => (
          <Space size="middle">
            <Popconfirm
              title={`${record.name}을 삭제하실 건가요?`}
              discription="삭제한 식물은 복구할 수 없어요"
              okText="네"
              cancelText="아니요"
              onConfirm={() => deletePlant(record.id)}
            >
              <DeleteOutlined/>
            </Popconfirm>
          </Space>
        )
      }
    ]
  )
}

/**
 * 장소 페이지 하단, 이 장소에 속한 식물들
 * -> ChangePlaceOfPlantOnPlace || AddPlantInPlaceButtons
 * @param plantList
 * @param setPlantList
 * @param placeName
 * @returns {JSX.Element}
 * @constructor
 */
const PlaceTableForPlant = ({placeName, plantListSize}) => {
  const placeId = useParams().placeId;

  // select
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const [plants, setPlants] = useState([]);

  const onSelectChange = (newSelectedRowKeys) => {
    setSelectedRowKeys(newSelectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  // 체크박스 눌렀는지
  const hasSelected = selectedRowKeys.length > 0;

  const deletePlant = async (plantId) => {
    console.log("deletePlant");
    console.log("plantId", plantId);
    await deleteData(`/plant/${plantId}`);
    const deletedList = plants.filter(plant => plant.id !== plantId);
    console.log("deletedList", deletedList);
    setPlants(() => deletedList);
  }

  const onMountPlaceTableForPlant = async (page) => {
    const plantList = (await getData(`/place/${placeId}/plant?page=${page - 1}`));
    setPlants(() => getPlantListForPlacePlantTable(plantList));
  }

  useEffect(() => {
    console.log("placeId changed")
    onMountPlaceTableForPlant();
  }, [placeId]);

  return (
    <div style={{marginTop: "1rem"}}>
      {
        hasSelected
          ? <ChangePlaceOfPlantOnPlace
            selectedPlantId={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
          />
          : <AddPlantInPlaceButtons
            placeName={placeName}
            setPlants={setPlants}
          />
      }

      <Table
        rowSelection={rowSelection}
        pagination={{onChange: (page) => onMountPlaceTableForPlant(page), total: plantListSize}}
        columns={getPlantTableColArrInPlace(deletePlant)}
        dataSource={plants}/>
    </div>
  )
}

export default PlaceTableForPlant;
