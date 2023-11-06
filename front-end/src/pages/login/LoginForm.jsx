import {CButton, CForm, CFormInput, CInputGroup, CInputGroupText, CRow} from "@coreui/react";
import CIcon from "@coreui/icons-react";
import {cilLockLocked, cilUser} from "@coreui/icons";
import React, {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from "axios";
import setLocalStorage from "../../api/service/setLocalStorage";
import {useDispatch} from "react-redux";

const LoginForm = () => {
  const dispatch = useDispatch();

  const [msg, setMsg] = useState('');

  const [login, setLogin] = useState({
    username: "testgardener",
    password: "testgardener123!"
  })

  // 입력 값 확인 및 submit
  const inputCheck = (e) => {
    e.preventDefault(); // reload 막기

    if (login.username === "") {
      setMsg("아이디를 입력해주세요")
    } else if (login.password === "") {
      setMsg("비밀번호를 입력해주세요")
    } else {
      submit();
    }
  }

  const navigate = useNavigate();

  const onChange = (e) => {
    const {name, value} = e.target;
    setLogin(setLogin => ({...login, [name]: value}))
  }

  const submit = async () => {
    try {
      const res = await axios.post(`${process.env.REACT_APP_API_URL}/login`, login);
      console.log("res", res);

      await setLocalStorage(res.data);
      dispatch({type: 'setName', name: res.data.simpleInfo.name});

      // garden 페이지로 이동
      navigate('/', {replace: true});
    } catch (error) {
      console.log("error", error);
      setMsg(error.response.data.message);
    }
  }

  return (
    <CRow>
      <h4 className="mt-3">로그인</h4>
      <p className="text-danger"><small>{msg}</small></p>
      <CForm onSubmit={inputCheck} method="POST">
        <CInputGroup className="mb-3">
          <CInputGroupText>
            <CIcon icon={cilUser}/>
          </CInputGroupText>
          <CFormInput placeholder="ID" name="username" onChange={onChange} defaultValue={login.username}/>
        </CInputGroup>
        <CInputGroup className="mb-2">
          <CInputGroupText>
            <CIcon icon={cilLockLocked}/>
          </CInputGroupText>
          <CFormInput name="password" type="password" placeholder="PW" onChange={onChange} defaultValue={login.password}/>
        </CInputGroup>
        <Link to="/forgot" className="text-decoration-none text-garden">
          <span style={{fontSize: 13}}><CIcon icon={cilLockLocked}/> 아이디/비밀번호 찾기</span>
        </Link>
        <CButton size="sm" type="submit" style={{border: 'none'}}
                 className="mt-3 bg-orange px-4 float-end">로그인</CButton>
      </CForm>
    </CRow>
  )
}

export default LoginForm;
