import {CButton, CCard, CCardBody} from "@coreui/react";
import CIcon from "@coreui/icons-react";

const ForgotAccountCard = (props) => {
  const {icon, type, color, setForgot} = props;

  let title = "비밀번호를 잊어버렸어요";

  if (type === "username") {
    title = "아이디를 잊어버렸어요";
  }

  return (
    <CCard
      style={{minHeight: "50vh"}}
      className={`bg-${color}`}
      onClick={() => setForgot(type)}>
      <CCardBody className="d-flex align-items-center justify-content-center">
        <div className="text-center">
          <div>
            <CIcon icon={icon} size="8xl" className={`${color === 'orange' ? "text-beige" : "text-orange"} mb-2`}/>
          </div>
          <CButton style={{border: 'none'}} className={`${color === 'orange' ? "bg-beige text-orange" : "bg-orange text-beige"} `}>{title}</CButton>
        </div>
      </CCardBody>
    </CCard>
  )
}

export default ForgotAccountCard;
