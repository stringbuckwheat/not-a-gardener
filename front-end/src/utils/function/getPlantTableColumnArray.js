import {Link} from "react-router-dom";
import {Popconfirm, Space, Tag} from "antd";
import {DeleteOutlined} from "@ant-design/icons";
import mediumArray from "../dataArray/mediumArray";
import PlantEditableCellAction from "../../pages/plant/PlantEditableCellAction";

const getColorIdxFromMediumArray = (medium) => {
  for (let i = 0; i < mediumArray.length; i++) {
    if (mediumArray[i].value === medium) {
      return i;
    }
  }
};

const getPlantTableColumnArray = (placeList, isEditing, cancel, edit, editingKey, updatePlant, deletePlant) => {

  return (
    [
      {
        title: '식물 이름',
        dataIndex: 'plantName',
        key: 'plantName',
        editable: true,
        render: (_, record) => {
          const link = `/plant/${record.plantNo}`;
          return (
            <Link
              to={link}
              className="no-text-decoration">
              {record.plantName}
            </Link>
          )
        },
        sorter: (a, b) => {
          if (a.plantName > b.plantName) return 1;
          if (a.plantName === b.plantName) return 0;
          if (a.plantName < b.plantName) return -1;
        },
        sortDirection: ['descend'],
      },
      {
        title: '식물 종',
        dataIndex: 'plantSpecies',
        key: 'plantSpecies',
        responsive: ['lg'],
        editable: true,
        sorter: (a, b) => {
          if (a.plantSpecies > b.plantSpecies) return 1;
          if (a.plantSpecies === b.plantSpecies) return 0;
          if (a.plantSpecies < b.plantSpecies) return -1;
        },
        sortDirection: ['descend']
      },
      {
        title: '장소',
        dataIndex: 'placeName',
        key: 'placeName',
        responsive: ['lg'],
        editable: true,
        filters: placeList.map((place) => ({
          text: place.placeName,
          value: place.placeName
        })),
        onFilter: (value, record) => record.placeName.indexOf(value) == 0,
      },
      {
        title: '물주기',
        dataIndex: 'averageWateringPeriod',
        key: 'averageWateringPeriod',
        responsive: ['lg'],
        sorter: (a, b) => {
          if (a.averageWateringPeriod > b.averageWateringPeriod) return 1;
          if (a.averageWateringPeriod === b.averageWateringPeriod) return 0;
          if (a.averageWateringPeriod < b.averageWateringPeriod) return -1;
        },
        sortDirection: ['descend']
      },
      {
        title: '태그',
        key: 'tags',
        dataIndex: 'tags',
        responsive: ['lg'],
        render: (tags) => {
          return (
            <>
                <div><Tag className="mb-1" color="green">{tags.medium}</Tag></div>
                <div><Tag className="mb-1" color="purple">"메시지 메시지 메시지 ㅁㄴㅇㄹㅁㄴㅇㄹ"</Tag></div>
                {
                  tags.anniversary
                    ? <div><Tag className="mb-1" color="cyan">{tags.anniversary}</Tag></div>
                    : <></>
                }
                {
                  tags.latestWateringDate
                    ? <div><Tag className="mb-1" color="geekblue">마지막 관수: {tags.latestWateringDate}</Tag></div>
                    : <></>
                }
            </>
          )
        },
      },
      {
        title: '등록일',
        dataIndex: 'createDate',
        key: 'createDate',
        responsive: ['lg'],
        sorter: (a, b) => {
          const d1 = new Date(a.createDate);
          const d2 = new Date(b.createDate);

          return d1 - d2;
        },
        sortDirection: ['descend']
      },
      {
        title: '',
        dataIndex: '',
        key: '',
        render: (_, record) => {
          const editable = isEditing(record);
          return (
            <PlantEditableCellAction
              record={record}
              editable={editable}
              cancel={cancel}
              edit={edit}
              editingKey={editingKey}
              updatePlant={updatePlant}
              deletePlant={deletePlant}
            />
          )
        }
      }
    ]
  )
}

export default getPlantTableColumnArray;
