import WateringListAction from "../../pages/plant/plantDetail/watering/WateringListAction";

const getWateringTableColumnArray = (isEditing, updateWatering, editingKey, cancel, edit, deleteWatering) => {

  return (
    [
      {
        title: '언제',
        dataIndex: 'wateringDate',
        key: 'wateringDate',
        editable: true,
      },
      {
        title: '무엇을',
        dataIndex: 'chemicalName',
        key: 'chemicalName',
        editable: true,
      },
      {
        title: '관수 간격',
        dataIndex: 'period',
        key: 'period',
      },
      {
        title: '',
        key: 'action',
        render: (_, record) => {
          const editable = isEditing(record);

          return <WateringListAction
            record={record}
            editable={editable}
            updateWatering={updateWatering}
            editingKey={editingKey}
            cancel={cancel}
            edit={edit}
            deleteWatering={deleteWatering}/>
        },
      },
    ]
  )
}

export default getWateringTableColumnArray;
