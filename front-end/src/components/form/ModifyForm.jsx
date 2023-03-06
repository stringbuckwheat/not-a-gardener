import { CContainer, CForm, CButton, CCol, CCard, CCardBody } from "@coreui/react";
import { useState } from "react";
import FormInputHandler from "./FormInputHandler";
import DeleteModal from "../modal/DeleteModal";
import ModifySubmitButton from "../button/ModifySubmitButton";

const ModifyForm = (props) => {
    //// props
    // title: 식물, 비료, 살충제...
    const title = props.title;

    // 제출용 객체
    const [inputObject, setInputObject] = useState(props.inputObject);

    // input 만들 정보가 담긴 배열
    const itemObjectArray = props.itemObjectArray;

    // 빈칸 검사를 해야 할 input 목록
    const requiredValueArray = props.requiredValueArray;

    // 숫자인지 검사해야 할 input 목록
    const isNumberArray = props.isNumberArray;

    // 제출할 url
    const url = props.submitUrl;

    // path -- addForm과 다른 점
    // PathVariable
    const path = props.path;

    // 수정 취소 버튼(뒤로가기)의 onClick 함수
    const onClickGetBackBtn = props.onClickGetBackBtn;

    //// ModifyForm의 변수와 함수
    // form이 유효성 검사를 완료했는지
    const [validation, setValidation] = useState(false);

    const onChange = (e) => {
        const { name, value } = e.target;

        // 유효성 검사
        if (requiredValueArray.includes(name)) {
            setValidation(value !== "");
        } else if (isNumberArray.includes(name)) {
            setValidation(Number.isInteger(value * 1));
        } else {
            setValidation(true);
        }

        setInputObject(setInputObject => ({ ...inputObject, [name]: value }));
    }

    return (
        <CContainer>
            <div className="row justify-content-md-center">
                <CCol md="auto">
                    <CCard sm={6} className="mb-4">
                        <CCardBody>
                            <div>
                                <h4 className="mt-3">{title} 수정</h4>
                                <div className="d-flex justify-content-end">
                                    <CButton onClick={onClickGetBackBtn} type="button" color="success" variant="outline" size="sm">뒤로 가기</CButton>
                                </div>
                            </div>
                            <CForm validated={true}>
                                {/* input, select 등을 구해서 채움 */}
                                <FormInputHandler
                                    itemObjectArray={itemObjectArray}
                                    onChange={onChange}
                                    inputObject={inputObject} />

                                {/* 수정 제출 버튼 */}
                                <div className="d-flex justify-content-end">
                                    <ModifySubmitButton
                                        url={url}
                                        path={path}
                                        data={inputObject}
                                        changeModifyState={onClickGetBackBtn}
                                        validation={validation} />
                                </div>
                            </CForm>
                        </CCardBody>
                    </CCard>
                </CCol>
            </div>
        </CContainer>
    )
}

export default ModifyForm;