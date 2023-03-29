import {Popconfirm, Space, Table, Tag} from 'antd';
import {useState} from 'react';
import ModifyPlantPlaceForm from '../form/ModifyPlantPlaceForm';
import AddPlantInPlaceBtns from '../form/AddPlantInPlaceBtns';
import {Link} from "react-router-dom";
import {DeleteOutlined} from "@ant-design/icons";
import getPlantListForPlacePlantTable from "../../utils/function/getPlantListForPlacePlantTable";
import deleteData from "../../api/backend-api/common/deleteData";

const PlaceTableForPlant = (props) => {
  const {plantList, setPlantList} = props;

  // select
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const onSelectChange = (newSelectedRowKeys) => {
    setSelectedRowKeys(newSelectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  const hasSelected = selectedRowKeys.length > 0;

  const plantTableColArrInPlace = [
    {
      title: '식물 이름',
      dataIndex: 'plantName',
      key: 'plantName',
      render: (_, record) => (
        <Link to={`/plant/${record.plantNo}`} className="text-decoration-none">
          {record.plantName}
        </Link>
      )
    },
    {
      title: '식물 종',
      dataIndex: 'plantSpecies',
      key: 'plantSpecies',
      responsive: ['lg']
    },
    {
      title: '평균 물주기',
      dataIndex: 'averageWateringPeriod',
      key: 'averageWateringPeriod',
      responsive: ['lg']
    },
    {
      title: '태그',
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
            title={`${record.plantName}을 삭제하실 건가요?`}
            discription="삭제한 식물은 복구할 수 없어요"
            okText="네"
            cancelText="아니요"
            onConfirm={() => deletePlant(record.plantNo)}
          >
            <DeleteOutlined/>
          </Popconfirm>
        </Space>
      )
    }
  ];

  const deletePlant = (plantNo) => {
    deleteData("/plant", plantNo);
    const deletedList = plantList.filter(plant => plant.plantNo !== plantNo);
    setPlantList(deletedList);
  }

  return (
    <div className="mt-4">
      {
        hasSelected
          ? <ModifyPlantPlaceForm
            selectedPlantNo={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
          />
          : <AddPlantInPlaceBtns
            plantList={plantList}
            setPlantList={setPlantList}/>
      }
      <Table
        columns={plantTableColArrInPlace}
        dataSource={getPlantListForPlacePlantTable(plantList)}
        rowSelection={rowSelection} // for multi-select
      />
    </div>
  )
}

export default PlaceTableForPlant;
