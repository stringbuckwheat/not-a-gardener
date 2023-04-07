import {Form, Input, Select} from "antd";

const PlantEditableCell = (props) => {
  const {
    placeList,
    modifyPlant,
    setModifyPlant,
    editing,
    dataIndex,
    title,
    inputType,
    record,
    index,
    children,
    ...restProps
  } = props;

  const onChange = (e) => {
    const {name, value} = e.target;
    setModifyPlant({...modifyPlant, [name]: value});
  }


  const inputNode = inputType === 'text'
    ? <Input
      name={dataIndex}
      onChange={(e) => {onChange(e)}}/>
    : <Select
      name="placeNo"
      style={{minWidth: 100}}
      options={placeList.map((place) => ({value: place.placeNo, label: place.placeName}))}
      onChange={(value) => {
        setModifyPlant({...modifyPlant, placeNo: value});
      }}
    />;

  return (
    <td {...restProps}>
      {editing
        ? (
          <Form.Item
            name={dataIndex}
            style={{margin: 0}}
            rules={[{required: true, message: "날짜를 입력해주세요",},]}
          >
            {inputNode}
          </Form.Item>
        ) : (
          children
        )}
    </td>
  )
}

export default PlantEditableCell;
