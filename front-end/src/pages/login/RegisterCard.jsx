import {CButton, CCard, CCardBody, CImage} from "@coreui/react";
import sprout from "../../assets/images/sprout.png";
import {Link} from "react-router-dom";

const RegisterCard = () => {
  return (
    <CCard
      className="text-white bg-primary py-5"
      sm={{width: '100%'}}
      lg={{width: '44%'}}>
      <CCardBody className="text-center">
        <h2>not-a-gardener</h2>
        <CImage
          fluid
          src={sprout}
          className="width-half"
        />
        <p>
          함께 키워요!
        </p>
        <Link to="/register">
          <CButton type="button" color="primary" className="mt-3" active tabIndex={-1}>
            가입하세요
          </CButton>
        </Link>
      </CCardBody>
    </CCard>
  )
}

export default RegisterCard;
