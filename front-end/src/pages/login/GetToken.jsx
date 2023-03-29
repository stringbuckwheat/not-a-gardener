import React, {useEffect} from 'react'
import {useNavigate, useParams} from 'react-router-dom';
import authAxios from '../../utils/interceptors'
import getData from "../../api/backend-api/common/getData";


const GetToken = () => {
  let {token} = useParams();
  const navigate = useNavigate();

  const setUser = async () => {
    localStorage.setItem("login", token);

    const user = await getData("/member/member-info");

    localStorage.setItem("memberNo", user.memberNo);
    localStorage.setItem("name", user.name);

    navigate("/", {replace: true});
  }

  useEffect(() => {
    setUser();
  }, [])

  return (
    <></>
  )
}

export default GetToken;
