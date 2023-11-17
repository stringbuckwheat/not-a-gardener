import {Input} from "antd";
import {LockOutlined} from "@ant-design/icons";

const CheckPrevPassword = ({pwCheck, password, onChange}) => {
  return (
    <>
      {
        pwCheck ? <></> : <span style={{fontSize: "12px"}} className="text-danger">비밀번호를 다시 확인해주세요</span>
      }
      <Input
        placeholder="기존 비밀번호를 입력해주세요"
        type="password"
        value={password}
        prefix={<LockOutlined/>}
        onChange={onChange}
      />
    </>
  )
}

export default CheckPrevPassword;
