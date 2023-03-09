import { Table } from 'antd';
import { useState } from 'react';
import ModifyPlantPlaceButton from '../button/ModifyPlantPlaceButton';

const DefaultTable = (props) => {
  const path = props.path; // pathVariables

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
      {
        hasSelected
        ?  <ModifyPlantPlaceButton
        selectedPlantNo={selectedRowKeys}
        placeNo={path} />
        : <></>
      }
      <Table
        className="mt-3"
        columns={props.columns}
        dataSource={props.list}
        rowSelection={rowSelection} // for multi-select
      />
    </div>
  )
}

export default DefaultTable;