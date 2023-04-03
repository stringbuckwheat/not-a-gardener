import {useLocation} from "react-router-dom";
import {useEffect, useState} from "react"
import ChemicalTag from "src/components/tag/ChemicalTag";
import DetailLayout from "src/components/data/layout/DetailLayout";
import {Table} from "antd";
import ItemForm from "src/components/form/ItemForm";
import ModifyFormButtons from "src/components/button/ModifyFormButtons";
import onMount from "src/api/service/onMount";
import getChemicalFormArray from "../../utils/function/getChemicalFormArray";
import wateringTableColumnArray from "../../utils/dataArray/wateringTableColumnInChemicalArray";

const ChemicalDetail = () => {
  const state = useLocation().state;

  const [wateringList, setWateringList] = useState([]);
  const [chemical, setChemical] = useState(state);
  const [onModify, setOnModify] = useState(false);

  useEffect(() => {
    onMount(`/chemical/${state.chemicalNo}/watering`, setWateringList);
  }, []);

  const onChange = (e) => {
    const {name, value} = e.target;
    setChemical(setChemical => ({...chemical, [name]: value}));
  }

  const validation = chemical.chemicalName != ''
    && Number.isInteger(chemical.chemicalPeriod * 1)
    && (chemical.chemicalPeriod * 1) > 0;

  const onClickModifyBtn = () => {
    setOnModify(!onModify);
  }

  const changeModifyState = () => {
    setOnModify(false);
  }

  return (
    !onModify
      ?
      <DetailLayout
        title={chemical.chemicalName}
        url="/chemical"
        path={state.chemicalNo}
        deleteTitle="비료/살균/살충제"
        tags={<ChemicalTag chemical={chemical} wateringListSize={wateringList.length}/>}
        onClickModifyBtn={onClickModifyBtn}
        deleteTooltipMsg="삭제한 약품은 되돌릴 수 없습니다"
        bottomData={<Table
          columns={wateringTableColumnArray}
          dataSource={wateringList}/>}
      />
      :
      <ItemForm
        title="비료/살균/살충제 수정"
        inputObject={chemical}
        itemObjectArray={getChemicalFormArray(state)}
        onChange={onChange}
        submitBtn={<ModifyFormButtons
          data={chemical}
          url="/chemical"
          path={state.chemicalNo}
          changeModifyState={changeModifyState}
          validation={validation}/>}/>
  )
}

export default ChemicalDetail;
