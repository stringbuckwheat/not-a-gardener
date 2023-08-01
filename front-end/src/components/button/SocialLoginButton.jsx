import {CCol} from "@coreui/react";
import {useSpring, animated} from "@react-spring/web";

/**
 * 소셜 로그인 버튼들(카카오, 구글, 네이버)
 * @param provider kakao, google, naver
 * @param recentLogin 최근 해당 소셜로그인 기록이 있으면 알려줌
 * @returns {JSX.Element}
 * @constructor
 */
const SocialLoginButton = ({provider, recentLogin} ) => {
  // const authorizationUrl = `http://localhost:80/oauth2/authorization`;
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
    <CCol xs={4} className={"text-center"}>
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
    </CCol>
  )
}

export default SocialLoginButton;
