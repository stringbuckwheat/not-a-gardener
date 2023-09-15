import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react"
import ChemicalTag from "src/components/tag/ChemicalTag";
import DetailLayout from "src/components/data/layout/DetailLayout";
import {Table} from "antd";
import FormProvider from "src/components/form/FormProvider";
import ModifyFormButtons from "src/components/button/ModifyFormButtons";
import getChemicalFormArray from "../../utils/function/getChemicalFormArray";
import wateringTableColumnArray from "../../utils/dataArray/wateringTableColumnInChemicalArray";
import RemoveModal from "../../components/modal/RemoveModal";
import deleteData from "../../api/backend-api/common/deleteData";
import getData from "../../api/backend-api/common/getData";

const ChemicalDetail = () => {
  const chemicalId = useParams().chemicalId;
  const state = useLocation().state;

  const [chemical, setChemical] = useState({});
  const [wateringList, setWateringList] = useState([]);
  const [modifyChemical, setModifyChemical] = useState(chemical);
  const [onModify, setOnModify] = useState(false);

  const navigate = useNavigate();

  const onMountChemicalDetail = async () => {
    try {
      const res = await getData(`/chemical/${chemicalId}`);

      console.log("res", res);
      setChemical(res.chemical);
      setModifyChemical(res.chemical);
      setWateringList(res.waterings);
    } catch (e) {
      if(e.code === "B006"){
        alert("해당 약품/비료를 찾을 수 없어요");
        navigate("/chemical");
      }
    }

  }

  useEffect(() => {
    onMountChemicalDetail();
  }, []);

  useEffect(() => {
    console.log("state", state);
    if(state == null){
      return;
    }

    setChemical(state);
    setModifyChemical(state);
  }, [state]);

  const onChange = (e) => {
    const {name, value} = e.target;
    setModifyChemical(setChemical => ({...modifyChemical, [name]: value}));
  }

  const validation = modifyChemical.name != ''
    && Number.isInteger(modifyChemical.period * 1)
    && (modifyChemical.period * 1) > 0;

  const deactivate = async () => {
    await deleteData(`/chemical/${chemical.id}/deactivate`);
    navigate("/chemical", {replace: true});
  }

  return !onModify
    ? (
      <DetailLayout
        title={chemical.name}
        url="/chemical"
        path={chemicalId}
        deleteTitle="비료/살균/살충제"
        tags={<ChemicalTag chemical={chemical} wateringListSize={wateringList.length}/>}
        onClickModifyBtn={() => setOnModify(!onModify)}
        deleteModal={
          <RemoveModal
            remove={deactivate}
            modalTitle={"이 비료/살균/살충제를 삭제하실 건가요?"}
            deleteButtonTitle={"삭제하기"}
            modalContent={"물주기 기록은 보존됩니다."}/>}
        bottomData={<Table
          columns={wateringTableColumnArray}
          dataSource={wateringList}/>}
      />)
    :
    (<FormProvider
        title="비료/살균/살충제 수정"
        inputObject={modifyChemical}
        itemObjectArray={getChemicalFormArray(chemical)}
        onChange={onChange}
        submitBtn={<ModifyFormButtons
          data={modifyChemical}
          url={`/chemical/${chemicalId}`}
          changeModifyState={() => setOnModify(false)}
          validation={validation}/>}/>
    )
}

export default ChemicalDetail;
