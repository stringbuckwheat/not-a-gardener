import {CCol, CRow} from "@coreui/react";
import React from "react";
import {animated, useSpring} from "@react-spring/web";

const SocialLogin = () => {
  const recentLogin = localStorage.getItem("provider");
  const providers = ["kakao", "google", "naver"];
  const authorizationUrl = `/oauth2/authorization`;

  const springProps = useSpring({
    display: 'inline',
    loop: {reverse: true},
    from: {y: 0},
    to: {y: 2},
    config: {
      tension: 300,
      friction: 30,
    }
  })

  return (
    <CRow className='mt-5'>
      <h6>간편 로그인</h6>
      <hr/>
      {
        providers.map((provider, index) =>{

          return (<CCol xs={4} className={"text-center"}>
          <a
            href={provider !== "naver" ? `http://localhost:8080${authorizationUrl}/${provider}` : "javascript:alert('검수 승인 대기중입니다')"}
            className="social-button"
            id={`${provider}-connect`}>
          </a>
          {
            provider === recentLogin
              ?
              <animated.div style={springProps}>
                <p style={{fontSize: "12px", fontWeight: "bold"}}>최근 로그인</p>
              </animated.div>
              : <></>
          }
        </CCol>)}
        )
      }
    </CRow>
  )
}

export default SocialLogin;
