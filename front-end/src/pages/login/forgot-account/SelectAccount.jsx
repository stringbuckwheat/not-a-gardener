import {Button, Radio, Space} from "antd";
import React, {useState} from "react";
import ChangePassword from "./ChangePassword";

/**
 * 비밀번호 찾기를 누른 유저의 신원확인 후 비밀번호를 변경할 아이디를 보여주는 페이지
 * @param email 해당 유저의 이메일
 * @param gardenerList 유저의 아이디 목록
 * @returns {JSX.Element} 계정 목록 or 비밀번호 찾기 페이지(ChangePassword)
 * @constructor
 */
const SelectAccount = ({email, gardenerList}) => {

  // 비밀번호를 바꿀 계정
  const [value, setValue] = useState(gardenerList[0]);
  // 비밀번호 변경 버튼을 눌렀는지
  const [isSelected, setIsSelected] = useState(false);

  const style = {fontSize: "0.9em"};

  // radio 선택 및 제출했으면 비밀번호 찾기 컴포넌트 렌더링
  return isSelected ? (
      <ChangePassword username={value}/>
    )
    : (
      <div>
        <p className="text-garden">[<b>{email}</b>]로 가입한 계정 목록이에요.</p>
        <Radio.Group style={{margin: "1rem"}} onChange={(e) => setValue(e.target.value)} value={value}>
          <Space direction="vertical">
            {
              gardenerList.map((gardener, index) =>
                <Radio value={gardener.username} key={index}>{gardener.username.replace(/(?<=.{3})./gi, '*')}</Radio>)
            }
          </Space>
        </Radio.Group>
        <p className="text-orange">비밀번호를 변경할 계정을 골라주세요</p>
        <Button
          type={"primary"}
          htmlType={"button"}
          onClick={() => setIsSelected(true)}
          className={"float-end"} style={{marginTop: "0.5rem", border: "0"}}>{"제출"}</Button>
      </div>
    )
}

export default SelectAccount
