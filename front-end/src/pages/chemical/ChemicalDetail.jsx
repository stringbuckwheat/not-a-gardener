import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react"
import ChemicalTag from "src/components/tag/ChemicalTag";
import DetailLayout from "src/components/data/layout/DetailLayout";
import {Table} from "antd";
import ItemForm from "src/components/form/ItemForm";
import ModifyFormButtons from "src/components/button/ModifyFormButtons";
import onMount from "src/api/service/onMount";
import getChemicalFormArray from "../../utils/function/getChemicalFormArray";
import wateringTableColumnArray from "../../utils/dataArray/wateringTableColumnInChemicalArray";
import RemoveModal from "../../components/modal/RemoveModal";
import deleteData from "../../api/backend-api/common/deleteData";

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

  const navigate = useNavigate();

  const remove = async () => {
    const res = await deleteData("/chemical", chemical.chemicalNo);
    navigate("/chemical", {replace: true});
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
        deleteModal={
          <RemoveModal
            remove={remove}
            modalTitle={"이 비료/살균/살충제를 삭제하실 건가요?"}
            deleteButtonTitle={"삭제하기"}
            modalContent={"이 비료/살균/살충제를 준 물주기 기록이 모두 삭제됩니다"} />}
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
