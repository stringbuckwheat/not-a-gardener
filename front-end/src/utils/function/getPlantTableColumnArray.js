import {Link, Navigate} from "react-router-dom";
import {Tag} from "antd";
import PlantEditableCellAction from "../../components/table/PlantEditableCellAction";
import WateringCodeIcon from "../../components/etc/WateringCodeIcon";
import ClickableTag from "../../components/tag/basic/ClickableTag";

const getPlantTableColumnArray = (placeList, isEditing, cancel, edit, editingKey, updatePlant, deletePlant) => {

  return (
    [
      {
        title: '상태',
        dataIndex: 'plantName',
        key: 'plantName',
        editable: true,
        render: (_, record) => {
          return (
            <WateringCodeIcon wateringCode={record.wateringCode} height={20} wateringMsg={record.tags.wateringMsg}/>
          )
        },
        sorter: (a, b) => a.wateringCode - b.wateringCode,
        sortDirection: ['descend'],
      },
      {
        title: '이름(종)',
        dataIndex: 'plantName',
        key: 'plantName',
        editable: true,
        render: (_, record) => (
          <>
            <Link
              to={`/plant/${record.plantNo}`}
              className="no-text-decoration">
              {record.plantName}
            </Link>
            {
              record.plantSpecies ? <p className="small">({record.plantSpecies})</p> : <></>
            }
          </>
        )
        ,
        sorter: (a, b) => {
          if (a.plantName > b.plantName) return 1;
          if (a.plantName === b.plantName) return 0;
          if (a.plantName < b.plantName) return -1;
        },
        sortDirection: ['descend'],
      },
      {
        title: '장소',
        dataIndex: 'placeName',
        key: 'placeName',
        responsive: ['lg'],
        editable: true,
        render: (_, record) => (
          <Tag color="green">{record.placeName}</Tag>
        ),
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
              {
                tags.anniversary
                  ? <div><Tag className="mb-1">{tags.anniversary}</Tag></div>
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
