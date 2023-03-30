import MemberDetail from "./MemberDetail";
import Loading from "../../components/data/Loading";
import {useEffect, useState} from "react";
import getData from "../../api/backend-api/common/getData";

const Member = () => {
  const memberNo = localStorage.getItem("memberNo");
  console.log("memberNo", memberNo);

  const [isLoading, setLoading] = useState(true);

  // 백엔드에서 받아온 회원 정보
  const [member, setMember] = useState({
    username: "",
    email: "",
    name: "",
    provider: "",
    createDate: ""
  });

  const onMountMember = async () => {
    const data = await getData(`/member/${memberNo}`);
    setMember(data);
    setLoading(false);
  }

  useEffect(() => {
    onMountMember();
  }, [])

  return isLoading ?
    (<Loading/>)
    :
    (
      <MemberDetail member={member} setMember={setMember}/>
    )
}

export default Member;
