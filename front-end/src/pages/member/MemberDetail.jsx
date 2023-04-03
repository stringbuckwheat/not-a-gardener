import {useState} from "react";
import {
  CContainer,
  CRow,
  CCol,
  CButton,
  CForm,
  CButtonGroup
} from '@coreui/react'
import updateData from "../../api/backend-api/common/updateData";
import FormInputFeedback from "../../components/form/input/FormInputFeedback";
import DeleteModal from "../../components/modal/DeleteModal";
import ChangePasswordModal from "../../components/modal/ChangePassWordModal";

const MemberDetail = (props) => {
  const {member, setMember} = props;

  const isBasicLogin = member.provider == null;

  // 수정용 input 칸 disabled 여부
  const [isDisabled, setIsDisabled] = useState(true);

  // 수정용 객체
  // 이메일과 이름만 변경 가능
  const [modifyMember, setModifyMember] = useState(member);

  const emailRegex = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/

  // 수정사항 반영
  const onChange = (e) => {
    const {name, value} = e.target;
    setModifyMember(setModifyMember => ({...modifyMember, [name]: value}));
  }

  const onSubmit = async () => {
    console.log("modifyMember: ", modifyMember);
    const updatedMember = await updateData("/member", member.memberNo, modifyMember);
    setMember(updatedMember);
    setIsDisabled(true);
  }

  const activateModifyInput = () => {
    setIsDisabled(!isDisabled)
  }

  const deleteButton = <CButton size="sm" color="link-secondary"><small>계정 삭제</small></CButton>;

  const deleteCallback = () => {
    localStorage.clear();
    window.location.replace('/');
  }

  return (
    <div className="d-flex flex-row align-items-center">
      <CContainer>
        <CRow className="d-flex align-items-center justify-content-center width-full">
          <CCol md={7}>
            <CForm>
              <h3 className="mt-3 mb-5 text-garden">회원 정보</h3>
              <FormInputFeedback
                label="이름"
                name="name"
                defaultValue={member.name}
                required
                valid={modifyMember.name !== ''}
                invalid={modifyMember.name === ''}
                feedbackInvalid="이름은 비워둘 수 없어요."
                onChange={onChange}
                disabled={isDisabled}/>

              <FormInputFeedback
                label="아이디"
                name="username"
                defaultValue={isBasicLogin ? member.username : member.provider}
                disabled={true}/>

              <FormInputFeedback
                label="이메일"
                name="email"
                defaultValue={member.email}
                onChange={onChange}
                disabled={isDisabled}
                required
                valid={emailRegex.test(modifyMember.email)}
                invalid={!emailRegex.test(modifyMember.email)}
                feedbackInvalid={modifyMember.email == "" ? "" : "이메일 형식을 확인해주세요"}/>

              <FormInputFeedback
                label="가입일"
                name="createDate"
                defaultValue={member.createDate.split("T")[0]}
                disabled={true}/>

              {isBasicLogin
                ?
                <div className="d-flex justify-content-end">
                  <CButtonGroup role="group" className="mt-3">
                    <CButton
                      size="sm"
                      type="button"
                      color="success"
                      variant="outline"
                      onClick={activateModifyInput}>
                      {isDisabled ? "회원정보 수정" : "돌아가기"}
                    </CButton>
                    {isDisabled
                      ?
                      <ChangePasswordModal/>
                      :
                      <CButton
                        size="sm"
                        type="button"
                        color={emailRegex.test(modifyMember.email) ? "success" : "secondary"}
                        disabled={!emailRegex.test(modifyMember.email)}
                        onClick={onSubmit}>
                        수정하기
                      </CButton>
                    }
                  </CButtonGroup>
                </div>
                : <>
                  <p><small>소셜 로그인 회원의 정보 수정은 해당 서비스를 이용해주세요</small></p>
                </>}
            </CForm>
            <div className="d-flex justify-content-end mt-3">
              <DeleteModal
                title="계정"
                url="/member"
                path={member.memberNo}
                button={deleteButton}
                deleteCallBackFunction={deleteCallback}
              />
            </div>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  );
}

export default MemberDetail;
