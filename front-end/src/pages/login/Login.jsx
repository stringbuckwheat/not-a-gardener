import {useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {
  CCard,
  CCardBody,
  CCardGroup,
  CCol,
  CContainer,
  CRow,
} from '@coreui/react'
import SocialLogin from "./SocialLogin";
import LoginForm from "./LoginForm";
import RegisterCard from "./RegisterCard";
import getLogin from "../../api/backend-api/member/getLogin";

const Login = () => {
  const navigate = useNavigate();

  const [login, setLogin] = useState({
    username: "",
    pw: ""
  })

  const [msg, setMsg] = useState('');

  const onChange = (e) => {
    const {name, value} = e.target;
    setLogin(setLogin => ({...login, [name]: value}))
  }

  const submit = async () => {
    try {
      await getLogin(login);

      // garden 페이지로 이동
      navigate('/', {replace: true});
    } catch (error) {
      setMsg(error.response.data.errorDescription);
    }
  }

  // 입력 값 확인 및 submit
  const inputCheck = (e) => {
    e.preventDefault(); // reload 막기

    if (login.username === "") {
      setMsg("아이디를 입력해주세요")
    } else if (login.pw === "") {
      setMsg("비밀번호를 입력해주세요")
    } else {
      submit();
    }
  }

  const sm = {width: '100%'}
  const lg = {width: '44%'}

  return (
    <div className="min-vh-100 d-flex align-items-center bg-garden">
      <CContainer>
        <CRow className="justify-content-center">
          <CCol md={8}>
            <CCardGroup>
              <CCard className="p-4">
                <CCardBody>
                  <h4 className="mt-3">로그인</h4>
                  <p className="text-danger"><small>{msg}</small></p>
                  <LoginForm inputCheck={inputCheck} onChange={onChange}/>
                  <SocialLogin/>
                </CCardBody>
              </CCard>
              <CCard
                className="text-garden py-5 d-flex align-items-center"
                sm={sm}
                lg={lg}>
                <CCardBody className="text-center">
                  <RegisterCard/>
                </CCardBody>
              </CCard>
            </CCardGroup>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  )
}

export default Login
