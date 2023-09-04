import React, {useEffect} from 'react'
import {useNavigate, useParams} from 'react-router-dom';
import getData from "../../api/backend-api/common/getData";
import setGardener from "../../api/service/setGardener";

const GetToken = () => {
  let {accessToken} = useParams();
  const navigate = useNavigate();

  const setUser = async () => {
    await localStorage.setItem("accessToken", accessToken);
    const res = await getData(`${process.env.REACT_APP_API_URL}/info`);

    res.token.accessToken = accessToken;
    await setGardener(res);

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
