import {Radio, Space} from "antd";
import React, {useState} from "react";
import Button from "../../../components/button/defaultButton/Button";
import ChangePassword from "./ChangePassword";

const SelectAccount = ({email, gardenerList}) => {
  // console.log("gardenerList", gardenerList);
  const [value, setValue] = useState(gardenerList[0]);
  const [isSelected, setIsSelected] = useState(false);

  const style = {fontSize: "0.9em"};

  return isSelected ? (
      <ChangePassword username={value}/>
    )
    : (
      <div>
        <p className="text-garden" style={style}>[<b>{email}</b>]로 가입한 계정 목록이에요.</p>
        <Radio.Group className="mb-3" onChange={(e) => setValue(e.target.value)} value={value}>
          <Space direction="vertical">
            {
              gardenerList.map((username, index) =>
                <Radio value={username} key={index}>{username.replace(/(?<=.{3})./gi, '*')}</Radio>)
            }
          </Space>
        </Radio.Group>
        <p className="text-orange" style={style}>비밀번호를 변경할 계정을 골라주세요</p>
        <Button onClick={() => setIsSelected(true)} name="제출" size="sm" color="orange" className="float-end mt-2"/>
      </div>
    )
}

export default SelectAccount
