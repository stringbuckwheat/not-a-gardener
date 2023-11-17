import ChangePasswordModal from "../../components/modal/ChangePassWordModal";
import DeleteModal from "../../components/modal/DeleteModal";
import {Button, ConfigProvider} from "antd";

const GardenerDetailBtns = ({gardenerId, isBasicLogin, onClickEdit, isDisabled, isValidEmail, onSubmit}) => {
  const deleteCallback = () => {
    localStorage.clear();
    window.location.replace('/login');
  }

  const theme = {
    components: {
      Button: {
        colorPrimary: "green",
        colorPrimaryHover: '#66B266', // green hover
      },
    }
  }

  return isBasicLogin ? (
    <ConfigProvider theme={theme}>
      <div style={{display: "flex", justifyContent: "flex-end"}}>
        <Button
          type={"primary"}
          onClick={onClickEdit}>
          {isDisabled ? "회원정보 수정" : "돌아가기"}
        </Button>
        {isDisabled
          ?
          <ChangePasswordModal/>
          :
          <Button
            type="primary"
            color={isValidEmail ? "success" : "secondary"}
            disabled={!isValidEmail}
            onClick={onSubmit}>
            수정하기
          </Button>
        }
      </div>
      <div style={{display: "flex", justifyContent: "flex-end", marginTop: "1rem"}}>
        <DeleteModal
          title="계정"
          url="/gardener"
          path={gardenerId}
          button={<Button type={"text"}><small>계정 삭제</small></Button>}
          deleteCallBackFunction={deleteCallback}
        />
      </div>
    </ConfigProvider>
  ) : (
    <p><small>소셜 로그인 회원의 정보 수정은 해당 서비스를 이용해주세요</small></p>
  )
}

export default GardenerDetailBtns
