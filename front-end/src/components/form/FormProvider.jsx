import {Card, Col, Form, Row} from "antd";
import InputHandler from "./InputHandler";

/**
 * Form 자동화 컴포넌트
 * @param title 제목
 * @param itemObjectArray Form을 만들 정보가 담긴 배열
 * @param submitBtn 제출 버튼
 * @param inputObject
 * @param onChange
 * @returns {JSX.Element}
 * @constructor
 */
const FormProvider = ({title, itemObjectArray, submitBtn, inputObject, onChange}) => {
  return (
    <Row style={{justifyContent: "center", alignItems: "center", height: "95%"}}>
      <Col md={12} xs={24} style={{minWidth: "40%"}}>
        <Card md={6} style={{marginBottom: "1.5rem"}}>
          <h4 className={"text-garden"}>{title}</h4>
          <Form layout="vertical">
            {/* input, select 등을 구해서 채움 */}
            <InputHandler
              itemObjectArray={itemObjectArray}
              onChange={onChange}
              inputObject={inputObject}/>
            {/* 등록 제출 버튼 */}
            {submitBtn}
          </Form>
        </Card>
      </Col>
    </Row>
  );
}

export default FormProvider;
