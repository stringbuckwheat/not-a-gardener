import {Link} from "react-router-dom";
import {Tag} from "antd";
import PlantEditableCellAction from "../../pages/plant/plantDetail/PlantEditableCellAction";
import WateringCodeIcon from "../../components/etc/WateringCodeIcon";

const getPlantTableColumnArray = (placeList, isEditing, cancel, edit, editingKey, updatePlant, deletePlant) => {

  return (
    [
      {
        title: '상태',
        dataIndex: 'wateringCode',
        key: 'wateringCode',
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
        dataIndex: 'name',
        key: 'name',
        editable: true,
        render: (_, record) => (
          <>
            <Link
              to={`/plant/${record.id}`}
              className="no-text-decoration">
              {record.name}
            </Link>
            {
              record.species ? <p className="small">({record.species})</p> : <></>
            }
          </>
        )
        ,
        sorter: (a, b) => {
          if (a.name > b.name) return 1;
          if (a.name === b.name) return 0;
          if (a.name < b.name) return -1;
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
        filters: placeList.map((place) => {
          return (
            {
              text: place.label,
              value: place.value
            }
          )

        }),
        onFilter: (value, record) => (record.placeId == value),
      },
      {
        title: '물주기',
        dataIndex: 'recentWateringPeriod',
        key: 'recentWateringPeriod',
        responsive: ['lg'],
        sorter: (a, b) => {
          if (a.recentWateringPeriod > b.recentWateringPeriod) return 1;
          if (a.recentWateringPeriod === b.recentWateringPeriod) return 0;
          if (a.recentWateringPeriod < b.recentWateringPeriod) return -1;
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
