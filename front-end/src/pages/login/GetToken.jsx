import React, {useEffect} from 'react'
import {useNavigate, useParams} from 'react-router-dom';
import authAxios from '../../api/interceptors'
import getData from "../../api/backend-api/common/getData";


const GetToken = () => {
  let {accessToken} = useParams();
  const navigate = useNavigate();

  const setUser = async () => {
    localStorage.setItem("accessToken", accessToken);

    const user = await getData("/gardener/gardener-info");

    localStorage.setItem("gardenerId", user.gardenerId);
    localStorage.setItem("name", user.name);
    localStorage.setItem("provider", user.provider);

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
