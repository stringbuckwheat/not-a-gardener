import React, { useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom';
import authAxios from '../../utils/interceptors'


const GetToken = () => {
    console.log("getToken")

    let { token } = useParams();
    const navigate = useNavigate();

    console.log("token", token);

    useEffect(function(){
        localStorage.setItem("login", token);

        authAxios.get("/member/member-info")
        .then((res) => {
            console.log("res", res);

            localStorage.setItem("memberNo", res.data.memberNo);
            localStorage.setItem("name", res.data.name);

            navigate("/", true);
        })
        .catch((error) => {
            console.log("error", error);
        });
    }, [])
    
    return (
        <></>
    )
}

export default GetToken;