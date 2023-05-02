import {Form, Input, Select} from "antd";
import {useContext, useEffect, useRef, useState} from "react";

/**
 * 식물 리스트에서 바로 수정하기위한 수정가능한 테이블 셀
 * @param editingKey
 * @param editable
 * @param updatePlant
 * @param editableContext
 * @param placeList
 * @param dataIndex
 * @param title
 * @param inputType
 * @param record
 * @param index
 * @param children
 * @param restProps
 * @returns {JSX.Element}
 * @constructor
 */
const PlantEditableCell = ({
                             editingKey,
                             editable,
                             updatePlant,
                             editableContext,
                             placeList,
                             dataIndex,
                             title,
                             inputType,
                             record,
                             index,
                             children,
                             ...restProps
                           }) => {
  const [editing, setEditing] = useState(false);
  const inputRef = useRef();
  const form = useContext(editableContext);

  useEffect(() => {
    record && setEditing(record.id === editingKey);
  }, [editingKey]);

  useEffect(() => {
    inputRef.current && inputRef.current.focus();
  }, [editing]);

  const inputNodeEditing = inputType === 'text'
    ?
    <Form.Item
      name={dataIndex}
      style={{margin: 0}}
      rules={[{required: true, message: "식물 이름을 입력해주세요",},]}
    >
      <Input
        ref={inputRef}
        name={dataIndex}/>
    </Form.Item>
    :
    <Form.Item
      name={"placeId"}
      style={{margin: 0}}
    >
      <Select
        style={{minWidth: 100}}
        options={placeList}
      />
    </Form.Item>;

  const childNode = editing ? inputNodeEditing : children;

  return <td {...restProps}>{childNode}</td>;

}

export default PlantEditableCell;
