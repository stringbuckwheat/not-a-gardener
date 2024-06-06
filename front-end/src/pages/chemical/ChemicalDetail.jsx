import {useLocation, useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react"
import ChemicalTag from "src/components/tag/ChemicalTag";
import DetailLayout from "src/components/data/layout/DetailLayout";
import getChemicalFormArray from "../../utils/function/getChemicalFormArray";
import deleteData from "../../api/backend-api/common/deleteData";
import getData from "../../api/backend-api/common/getData";
import ValidationSubmitButton from "../../components/button/ValidationSubmitButton";
import updateData from "../../api/backend-api/common/updateData";
import TableWithPage from "../../components/data/TableWithPage";
import wateringTableColumnArray from "../../utils/dataArray/wateringTableColumnInChemicalArray";
import FormProvider from "../../components/form/FormProvider";
import ExceptionCode from "../../utils/code/exceptionCode";

const ChemicalDetail = () => {
  const chemicalId = useParams().chemicalId;
  const state = useLocation().state;

  const [chemical, setChemical] = useState({});
  const [modifyChemical, setModifyChemical] = useState(chemical);
  const [onModify, setOnModify] = useState(false);
  const [wateringSize, setWateringSize] = useState(0);

  const navigate = useNavigate();

  const onMountChemicalDetail = async () => {
    console.log("ONMOUNTCHEMICALDETAIL")

    try {
      const res = await getData(`/chemical/${chemicalId}`);

      console.log("res", res);
      setChemical(res.chemical);
      setWateringSize(res.wateringSize);
      setModifyChemical(res.chemical);
    } catch (e) {
      if (e.code === ExceptionCode.NO_SUCH_CHEMICAL) {
        alert("해당 약품/비료를 찾을 수 없어요");
        navigate("/chemical");
      }
    }
  }

  useEffect(() => {
    onMountChemicalDetail();
  }, []);

  useEffect(() => {
    if (state == null) {
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
    await deleteData(`/chemical/${chemical.id}`);
    navigate("/chemical", {replace: true});
  }

  const submit = async () => {
    const res = await updateData(`/chemical/${chemicalId}`, modifyChemical);
    navigate("", {replace: true, state: res});
    setOnModify(false);
  }

  const getChemicalUsage = async (page) => {
    return (await getData(`/chemical/${chemicalId}/watering?page=${page - 1}`));
  }

  return !onModify
    ? (
      <DetailLayout
        title={chemical.name}
        url="/chemical"
        path={chemicalId}
        deleteTitle="비료/살균/살충제"
        tags={<ChemicalTag chemical={chemical} wateringListSize={wateringSize}/>}
        onClickModifyBtn={() => setOnModify(!onModify)}
        children={<TableWithPage columns={wateringTableColumnArray}
                                   getDataSource={getChemicalUsage}
                                   total={wateringSize}/>}
        detailMsg={"단, 물주기 기록은 보존됩니다."}
      />)
    :
    (<FormProvider
        title="비료/살균/살충제 수정"
        inputObject={modifyChemical}
        itemObjectArray={getChemicalFormArray(chemical)}
        onChange={onChange}
        submitBtn={
          <ValidationSubmitButton
            className="float-end"
            isValid={validation}
            title={"수정하기"}
            onClickValid={submit}
            onClickInvalidMsg={"입력값을 확인해주세요"}/>}
      />
    )
}

export default ChemicalDetail;
