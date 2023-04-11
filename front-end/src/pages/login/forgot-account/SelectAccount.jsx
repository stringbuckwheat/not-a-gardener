import {Radio, Space} from "antd";
import React, {useState} from "react";
import Button from "../../../components/button/defaultButton/Button";
import ChangePassword from "./ChangePassword";

const SelectAccount = ({email, memberList}) => {
  const [value, setValue] = useState(memberList[0]);
  const [isSelected, setIsSelected] = useState(false);
  const onChange = (e) => {
    setValue(e.target.value);
  };

  const onClick = () => {
    setIsSelected(true);
  }

  return isSelected ? (
      <ChangePassword username={value}/>
    )
    : (
      <div>
        <p className="text-garden" style={{fontSize: "0.9em"}}>[<b>{email}</b>]로 가입한 계정 목록이에요.</p>
        <Radio.Group className="mb-3" onChange={onChange} value={value}>
          <Space direction="vertical">
            {
              memberList.map((username) =>
                <Radio value={username}>{username.replace(/(?<=.{3})./gi, '*')}</Radio>)
            }
          </Space>
        </Radio.Group>
        <p className="text-orange" style={{fontSize: "0.9em"}}>비밀번호를 변경할 계정을 골라주세요</p>
        <Button onClick={onClick} name="제출" size="sm" color="orange" className="float-end mt-2"/>
      </div>
    )
}

export default SelectAccount
