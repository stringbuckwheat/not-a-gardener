import {CButton, CImage} from "@coreui/react";
import sprout from "../../assets/images/sprout.png";
import {Link} from "react-router-dom";

const RegisterCard = () => {

  return (
    <div className="height-95 d-flex align-items-center">
      <div>
        <h4 className="mt-3">not-a-gardener</h4>
        <CImage
          fluid
          src={sprout}
          className="width-half"
        />
        <p>
          함께 키워요!
        </p>
        <Link to="/register">
          <CButton type="button" color="primary" className="mt-3">
            가입하세요
          </CButton>
        </Link>
      </div>
    </div>
  )
}

export default RegisterCard;
