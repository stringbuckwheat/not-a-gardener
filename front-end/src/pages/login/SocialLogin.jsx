import React from "react";
import {animated, useSpring} from "@react-spring/web";
import {Col, Row} from "antd";

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
    <div style={{marginTop: "3.5rem"}}>
      <h5>간편 로그인</h5>
      <hr/>
      <Row justify={"space-between"} style={{marginTop: "1rem"}}>
        {
          providers.map((provider, index) => {
              return (<Col md={7} className={"text-center"}>
                <a
                  href={provider !== "naver" ? `${authorizationUrl}/${provider}` : "javascript:alert('검수 승인 대기중입니다')"}
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
              </Col>)
            }
          )
        }
      </Row>
    </div>
  )
}

export default SocialLogin;
