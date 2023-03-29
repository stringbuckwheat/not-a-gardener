import {useState} from 'react'
import {useNavigate} from 'react-router-dom'
import {
  CCard,
  CCardBody,
  CCardGroup,
  CCol,
  CContainer,
  CRow,
  CCardHeader
} from '@coreui/react'
import SocialLoginButton from "./SocialLoginButton";
import LoginForm from "./LoginForm";
import RegisterCard from "./RegisterCard";
import getLogin from "../../api/backend-api/member/getLogin";

function Login() {
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
      getLogin(login);

      // garden 페이지로 이동
      navigate('/');
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

  return (
    <div className="bg-light min-vh-100 d-flex align-items-center">
      <CContainer fluid>
        <CRow className="justify-content-center">
          <CCol md={8}>
            <CCardGroup>
              <CCard className="p-4">
                <CCardHeader className="mb-0">
                  <h2>로그인</h2>
                </CCardHeader>
                <CCardBody>
                  <p className="text-danger"><small>{msg}</small></p>
                  <LoginForm inputCheck={inputCheck} onChange={onChange}/>
                  <SocialLoginButton />
                </CCardBody>
              </CCard>
              <RegisterCard />
            </CCardGroup>
          </CCol>
        </CRow>
      </CContainer>
    </div>
  )
}

export default Login
