const getMergedColumns = (columnArray, conditionCol, trueInput, falseInput, isEditing) => {
  const mergedColumns = columnArray.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: (record) => {
        return {
        record,
        inputType: col.dataIndex === conditionCol ? trueInput : falseInput,
        dataIndex: col.dataIndex,
        title: col.title,
        editing: isEditing(record),
      }},
    };
  });

  return mergedColumns;
}

export default getMergedColumns;
