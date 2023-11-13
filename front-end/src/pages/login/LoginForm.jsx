import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import setLocalStorage from "../../api/service/setLocalStorage";
import {useDispatch} from "react-redux";
import {Button, Input} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";

const LoginForm = () => {
  const dispatch = useDispatch();

  const [msg, setMsg] = useState('');

  const [login, setLogin] = useState({
    username: "testgardener",
    password: "testgardener123!"
  })
  const navigate = useNavigate();

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

  const onChange = (e) => {
    const {name, value} = e.target;
    setLogin(setLogin => ({...login, [name]: value}))
  }

  const submit = async () => {
    console.log("submit click");
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


  useEffect(() => {
    setMsg("");
  }, [login.password, login.username]);

  return (
    <>
      <h4 className="mt-3">로그인</h4>
      <p className="text-danger"><small>{msg}</small></p>
      <form onSubmit={inputCheck} method="POST">
        <Input size="large" prefix={<UserOutlined style={{margin: "0 0.5rem"}}/>}
               placeholder="ID" name="username" onChange={onChange} defaultValue={login.username}/>
        <Input size="large" prefix={<LockOutlined style={{margin: "0 0.5rem"}}/>}
               style={{marginTop: "0.7rem"}}
               name="password" type="password" placeholder="PW" onChange={onChange} defaultValue={login.password}/>
        <Button type={"primary"} htmlType="submit" className={"float-end"}
                style={{width: "6rem", marginTop: "1rem"}}> 로그인</Button>
      </form>
    </>
  )
}

export default LoginForm;
