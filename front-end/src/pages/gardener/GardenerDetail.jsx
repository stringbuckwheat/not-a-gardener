import {useState} from "react";
import updateData from "../../api/backend-api/common/updateData";
import {useDispatch} from "react-redux";
import {Col, Row} from "antd";
import FormProvider from "../../components/form/FormProvider";
import GardenerDetailBtns from "./GardenerDetailBtns";

const GardenerDetail = ({gardener, setGardener}) => {
  console.log("최초 gardener", gardener);
  const dispatch = useDispatch();
  const isBasicLogin = gardener.provider == null;

  // 수정용 input 칸 disabled 여부
  const [isDisabled, setIsDisabled] = useState(true);

  // 수정용 객체
  // 이메일과 이름만 변경 가능
  const [modifyGardener, setModifyGardener] = useState(gardener);

  const emailRegex = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/

  // 수정사항 반영
  const onChange = (e) => {
    const {name, value} = e.target;
    setModifyGardener(() => ({...modifyGardener, [name]: value}));
  }

  const onSubmit = async () => {
    const updatedGardener = await updateData(`/gardener/${gardener.id}`, modifyGardener);
    console.log("updatedGardener", updatedGardener);
    setGardener(() => updatedGardener);

    // header 업데이트
    localStorage.setItem("name", updatedGardener.name);
    dispatch({type: 'setName', payload: updatedGardener.name});

    setIsDisabled(true);
  }

  const getSocialAccount = () => {
    if (gardener.provider === "kakao") {
      return "카카오"
    } else if (gardener.provider === "naver") {
      return "네이버"
    } else {
      return "구글"
    }
  }

  const gardenerDetail = [
    {
      label: "이름",
      name: "name",
      defaultValue: gardener.name,
      required: true,
      onChange: onChange,
      disabled: isDisabled,
    },
    {
      label: isBasicLogin ? "아이디" : "소셜 로그인",
      name: "username",
      defaultValue: isBasicLogin ? gardener.username : getSocialAccount(),
      disabled: true
    },
    {
      label: "이메일",
      name: "email",
      defaultValue: gardener.email,
      onChange: onChange,
      disabled: isDisabled,
      required: true,
      valid: emailRegex.test(modifyGardener.email),
      invalid: !emailRegex.test(modifyGardener.email),
      feedbackInvalid: "이메일 형식을 확인해주세요"
    },
    {
      label: "가입일",
      name: "createDate",
      defaultValue: gardener.createDate.split("T")[0],
      disabled: true
    }
  ]

  const onClickEdit = () => setIsDisabled(!isDisabled);
  const isValidEmail = emailRegex.test(modifyGardener.email)

  return (
    <Row>
      <Col md={24}>
        <FormProvider
          title="회원 정보"
          inputObject={modifyGardener}
          itemObjectArray={gardenerDetail}
          onChange={onChange}
          submitBtn={<GardenerDetailBtns
            gardenerId={gardener.id}
            isBasicLogin={isBasicLogin}
            onClickEdit={onClickEdit}
            isDisabled={isDisabled}
            isValidEmail={isValidEmail}
          />}
        />
      </Col>
    </Row>
  );
}

export default GardenerDetail;
