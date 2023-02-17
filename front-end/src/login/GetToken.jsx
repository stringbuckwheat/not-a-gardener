import React, { useState, useEffect, useCallback } from 'react'
import { useNavigate, useParams } from 'react-router-dom';

const GetToken = () => {
    let { token } = useParams();
    const navigate = useNavigate();

    useEffect(function(){
        localStorage.setItem("login", token);
        navigate("/garden");
    }, [])
    
   
    return (
        <></>
    )
}

export default GetToken;