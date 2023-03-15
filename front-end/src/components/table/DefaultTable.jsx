import { Table } from 'antd';
import { useState } from 'react';
import ModifyPlantPlaceForm from '../form/ModifyPlantPlaceForm';
import AddPlantsInPlaceForm from '../form/AddPlantsInPlaceForm';

const DefaultTable = (props) => {
  const path = props.path; // pathVariables
  const setPlantList = props.setPlantList;

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

  return (
    <div className="mt-4">
      {
        hasSelected
          ? <ModifyPlantPlaceForm
            selectedPlantNo={selectedRowKeys}
            setSelectedRowKeys={setSelectedRowKeys}
          />
          : <AddPlantsInPlaceForm
            setPlantList={setPlantList} />
      }
      <Table
        columns={props.columns}
        dataSource={props.list}
        rowSelection={rowSelection} // for multi-select
      />
    </div>
  )
}

export default DefaultTable;