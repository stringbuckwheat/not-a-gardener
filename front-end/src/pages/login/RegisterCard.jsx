import {CButton, CImage} from "@coreui/react";
import sprout from "../../assets/images/sprout.png";
import {Link} from "react-router-dom";

const RegisterCard = () => {

  return (
    <div className="height-95 d-flex align-items-center">
      <div>
        <h4 className="mt-3">가입하세요</h4>
        <CImage
          fluid
          src={sprout}
          className="width-half"
        />
        <p>
          아직 회원이 아니신가요?
        </p>
        <Link to="/register">
          <CButton type="button" style={{border: 'none'}} className="mt-3 bg-orange">
            가입하세요
          </CButton>
        </Link>
      </div>
    </div>
  )
}

export default RegisterCard;
