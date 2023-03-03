import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import authAxios from '../../utils/interceptors';
import {
    CButton,
    CCard,
    CCardBody,
    CCol,
    CContainer,
    CForm,
    CFormInput,
    CInputGroup,
    CInputGroupText,
    CRow,
    CButtonGroup
  } from '@coreui/react'
  import CIcon from '@coreui/icons-react'
  import { cilUser, cilCalendarCheck } from '@coreui/icons'
  import DeleteAccountModal from "./DeleteAccountModal";

const MemberDetail = () => {
    const memberNo = useParams().memberNo;
    console.log(memberNo);

    // 자체 가입 회원인지
    const [isBasicLogin, setIsBasicLogin] = useState(false);

    // 수정용 input 칸 disabled 여부
    const [ isDisabled, setIsDisabled ] = useState(true);

    // 백엔드에서 받아온 회원 정보
    const [memberDetail, setMemberDetail] = useState({
        username: "",
        email: "",
        name: "",
        provider: "",
        createDate: ""
    });

    // 수정용 객체
    // 이메일과 이름만 변경 가능
    const [ modifyMember, setModifyMember] = useState({});

    const initializeMemberVariables = (rawData) => {
        setMemberDetail({
            username: rawData.username,
            email: rawData.email,
            name: rawData.name,
            provider: rawData.provider,
            createDate: new Date(rawData.createDate).toLocaleDateString()
        })

        setModifyMember({
            memberNo: memberNo,
            email: rawData.email,
            name: rawData.name
        })
    }

    useEffect(() => {
        authAxios.get("/member/" + memberNo)
        .then((res) => {
            console.log(res);

            if(!res.data.provider){
                // 이 값이 null이면 자체 가입 회원
                console.log("provider", res.data.provider);
                setIsBasicLogin(true);
            }

            initializeMemberVariables(res.data);
        })
        .catch((error) => {
            console.log(error);
        })
    }, []);

    const emailRegex = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/

    // 수정사항 반영
    const onChange = (e) => {
        const { name, value } = e.target;
        setModifyMember(setModifyMember => ({...modifyMember, [name]: value}));
    }

    const onSubmit = () => {
        console.log("modifyMember", modifyMember);

        authAxios.put("/member/" + memberNo, modifyMember)
        .then((res) => {
            initializeMemberVariables(res.data);
            setIsDisabled(true);
        })
    }

    const activateModifyInput = () => {
        setIsDisabled(!isDisabled)
    }

    const [visible, setVisible] = useState(false);
    const closeDeleteModal = () => {
        setVisible(false);
    }

    return(
      <CContainer fluid>
        <CRow className="justify-content-center">
          <CCol md={9} lg={7} xl={6}>
            <CCard className="mx-4">
              <CCardBody className="p-4">
                <CForm>
                  <h3 className="mb-5">회원 정보</h3>

                  <CInputGroup className="mb-3">
                    <CInputGroupText>
                      <CIcon icon={cilUser} />
                    </CInputGroupText>
                    <CFormInput
                        name="name"
                        defaultValue={memberDetail.name}
                        onChange={onChange}
                        disabled={isDisabled}/>
                  </CInputGroup>

                  <CInputGroup className="mb-3">
                    <CInputGroupText>
                      <CIcon icon={cilUser} />
                    </CInputGroupText>
                    <CFormInput
                        name="username"
                        value={isBasicLogin ? memberDetail.username : memberDetail.provider}
                        disabled/>
                  </CInputGroup>

                  <CInputGroup className="mb-3">
                    <CInputGroupText>@</CInputGroupText>
                    <CFormInput
                        name="email"
                        defaultValue={memberDetail.email}
                        onChange={onChange}
                        disabled={isDisabled}
                    />
                  </CInputGroup>
                  <p>
                    <small>{
                        !emailRegex.test(modifyMember.email)
                        ? "이메일 형식을 확인해주세요"
                        : ""}
                    </small>
                  </p>
                  <CInputGroup className="mb-4">
                    <CInputGroupText>
                      <CIcon icon={cilCalendarCheck} />
                    </CInputGroupText>
                    <CFormInput
                      type="text"
                      name="createDate"
                      value={memberDetail.createDate}
                      disabled
                    />
                  </CInputGroup>
                    {isBasicLogin
                    ?
                    <div className="d-flex justify-content-end">
                        {isDisabled
                        ?
                        <CButtonGroup role="group">
                            <CButton type="button" color="success" variant="outline" onClick={activateModifyInput}>회원정보 수정</CButton>
                            <CButton color="success" variant="outline" >비밀번호 변경</CButton>
                        </CButtonGroup>
                        :
                        <CButtonGroup role="group">
                            <CButton type="button" color="success" variant="outline" onClick={activateModifyInput}>돌아가기</CButton>
                            {emailRegex.test(modifyMember.email)
                            ? <CButton type="button" color="success" onClick={onSubmit}>수정하기</CButton>
                            : <CButton type="button" color="secondary" disabled>수정하기</CButton>}
                        </CButtonGroup>
                        }
                    </div>
                    : <>
                    <p><small>소셜 로그인 회원의 정보 수정은 해당 서비스를 이용해주세요</small></p>
                    </>}
                </CForm>
                <div className="d-flex justify-content-end mt-3">
                <DeleteAccountModal visible={visible} memberNo={memberNo} closeDeleteModal={closeDeleteModal} />
                <CButton color="link-secondary" onClick={() => setVisible(true)}><small>계정 삭제</small></CButton>
                </div>
              </CCardBody>
            </CCard>
          </CCol>
        </CRow>
      </CContainer>
    );
}

export default MemberDetail;
