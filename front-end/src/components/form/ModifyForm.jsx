import { CForm, CCol, CCard, CCardBody, CRow } from "@coreui/react";
import { useState } from "react";
import FormInputHandler from "./FormInputHandler";
import ModifySubmitButton from "../button/ModifySubmitButton";
import { RollbackOutlined } from "@ant-design/icons";
import { Tooltip } from 'antd';

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

    // 수정 후 콜백 함수
    const callBackFunction = props.callBackFunction;

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
        <div className="row justify-content-md-center">
            <CCol md="auto" style={{ minWidth: '60%' }}>
                <CCard sm={6} className="mb-4" >
                    <CCardBody>
                        <CRow className="mb-3">
                            <CCol>
                                <h4 className="mt-3">{title} 수정</h4>
                            </CCol>
                            <CCol>
                                <div className="d-flex justify-content-end mt-4">
                                    <Tooltip title="뒤로가기">
                                        <RollbackOutlined 
                                            onClick={onClickGetBackBtn}
                                            style={{ fontSize: '18px', color: 'grey' }} />
                                    </Tooltip>
                                </div>
                            </CCol>
                        </CRow>
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
                                    callBackFunction={callBackFunction}
                                    validation={validation} />
                            </div>
                        </CForm>
                    </CCardBody>
                </CCard>
            </CCol>
        </div>
    )
}

export default ModifyForm;
