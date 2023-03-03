import { Space, Table, Tag } from 'antd';
import { useState } from 'react';
import mediumArray from 'src/utils/dataArray/mediumArray';
import { useNavigate } from 'react-router-dom';
import { cilPlant } from '@coreui/icons';
import CIcon from '@coreui/icons-react';
import ModifyPlantPlaceButton from '../button/ModifyPlantPlaceButton';
import getPlaceList from 'src/utils/function/getPlaceList';
import { useParams } from 'react-router-dom';

const DefaultTable = (props) => {
  const navigate = useNavigate();
  const placeNo = useParams().placeNo;

  const columns = [
    {
      title: '식물 이름',
      dataIndex: 'plantName',
      key: 'plantName',
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
      render: (_, { tags }) => (
        <>
          {tags.map((tag) => {
            // const mediumArray = ["흙과 화분", "수태", "반수경", "수경", "테라리움"];
            const colorArr = ["green", "orange", "volcano", "cyan", "geekblue"];

            // mediumArray의 index와 동일한 컬러
            const color = colorArr[mediumArray.indexOf(tag)];

            return (
              <Tag color={color} key={tag}>
                {tag}
              </Tag>
            );
          })}
        </>
      ),
    },
    {
      title: '자세히',
      dataIndex: 'plantDetail',
      key: 'plantDetail',
      render: (_, record) => (
        <Space size="middle">
          <CIcon icon={cilPlant} onClick={() => {onClickPlantDetail(record)}}/>
        </Space>
      )
    }
  ];

  const onClickPlantDetail = async (record) => {
    const placeList = await getPlaceList();
    console.log(placeList, "placelist");
    navigate("/plant/" + record.plantNo, { state: { data: record, placeList: placeList } });
  }

  // select
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const onSelectChange = (newSelectedRowKeys) => {
    console.log('selectedRowKeys changed: ', newSelectedRowKeys);
    setSelectedRowKeys(newSelectedRowKeys);
    console.log("selectedPlantNo", selectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  const hasSelected = selectedRowKeys.length > 0;

  return (
    <div className="mt-3">
    {hasSelected 
    ?
      <ModifyPlantPlaceButton 
        selectedPlantNo={selectedRowKeys}
        placeNo={placeNo}
        />
    : <></>
    }
      <Table
        className="mt-3"
        columns={columns}
        dataSource={props.list.map((rowData) => ({
          key: rowData.plantNo,
          plantNo: rowData.plantNo,
          plantName: rowData.plantName,
          plantSpecies: rowData.plantSpecies,
          averageWateringPeriod: rowData.averageWateringPeriod,
          tags: [rowData.medium],
          createDate: rowData.createDate
        }
        ))}
        rowSelection={rowSelection} // for multi-select
      />
    </div>
  )
}

export default DefaultTable;