import {CCol} from "@coreui/react";
import {useSpring, animated} from "@react-spring/web";

const SocialLoginButton = ({provider, recentLogin} ) => {
  const authorizationUrl = `${process.env.REACT_APP_API_URL}/oauth2/authorization`;
  console.log("authorizationUrl", authorizationUrl);

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
      <a href={`${authorizationUrl}/${provider}`} className="social-button" id={`${provider}-connect`}></a>
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
