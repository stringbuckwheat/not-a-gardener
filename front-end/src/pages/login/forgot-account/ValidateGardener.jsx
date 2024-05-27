import VerifyAccountContent from "./VerifyAccountContent";
import {Card} from "antd";

/**
 * 아이디/비밀번호 찾기의 첫 단계인 이메일 인증 페이지
 * 부모 컴포넌트: ForgotAccount
 * -> VerifyAccountContent
 * @param icon
 * @param title
 * @param setEmail
 * @param setGardenerList
 * @param successContent
 * @returns {JSX.Element}
 * @constructor
 */

const style = {minHeight: "40vh", height: "100%", justifyContent: "center", alignItems: "center", display: "flex",}
const ValidateGardener = ({icon, title, setGardenerList, successContent}) => {
  return (
    <>
      <div style={{fontSize: "2rem"}}>{icon} {title}</div>
      <Card style={style}>
        <VerifyAccountContent
          setGardenerList={setGardenerList}
          successContent={successContent}/>
      </Card>
    </>
  )
}

export default ValidateGardener;
