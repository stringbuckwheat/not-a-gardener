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
            <WateringCodeIcon wateringCode={record.wateringCode} size={"2rem"} wateringMsg={record.tags.wateringMsg}/>
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
              record.species ? <p><small>({record.species})</small></p> : <></>
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
        sorter: (a, b) => {
          if (a.recentWateringPeriod > b.recentWateringPeriod) return 1;
          if (a.recentWateringPeriod === b.recentWateringPeriod) return 0;
          if (a.recentWateringPeriod < b.recentWateringPeriod) return -1;
        },
        sortDirection: ['descend']
      },
      {
        title: '마지막 관수',
        key: 'tags',
        dataIndex: 'tags',
        render: (tags) => {
          return (
            <>
              {
                tags.anniversary
                  ? <div><Tag style={{marginBottom: "0.25rem"}}>{tags.anniversary}</Tag></div>
                  : <></>
              }
              {
                tags.latestWateringDate
                  ? <div><Tag style={{marginBottom: "0.25rem"}} color="geekblue">{tags.latestWateringDate}</Tag></div>
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
        responsive: ['lg'],
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
