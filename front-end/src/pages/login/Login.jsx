import {
  CCard,
  CCardBody,
} from '@coreui/react'
import SocialLogin from "./SocialLogin";
import LoginForm from "./LoginForm";
import RegisterCard from "./RegisterCard";
import LoginPageWrapper from "./LoginPageWrapper";

const Login = () => {
  const sm = {width: '100%'}
  const lg = {width: '44%'}

  return (
    <LoginPageWrapper>
      <CCard className="p-4">
        <CCardBody >
          <LoginForm/>
          <SocialLogin/>
        </CCardBody>
      </CCard>
      <CCard
        className="text-garden bg-beige py-5 d-flex align-items-center"
        sm={sm}
        lg={lg}>
        <CCardBody className="text-center">
          <RegisterCard/>
        </CCardBody>
      </CCard>
    </LoginPageWrapper>
  )
}

export default Login
