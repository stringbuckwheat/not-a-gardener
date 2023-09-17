import React, {useEffect} from 'react'
import {Navigate, useNavigate, useParams} from 'react-router-dom';
import getData from "../../api/backend-api/common/getData";
import setLocalStorage from "../../api/service/setLocalStorage";

const GetToken = () => {
  if (localStorage.getItem("accessToken")) {
    return <Navigate to="/" replace={true}/>
  }

  let {accessToken} = useParams();
  const navigate = useNavigate();

  const setUser = async () => {
    await localStorage.setItem("accessToken", accessToken);
    const res = await getData(`${process.env.REACT_APP_API_URL}/info`);

    res.token.accessToken = accessToken;
    await setLocalStorage(res);

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
