import GardenerDetail from "./GardenerDetail";
import Loading from "../../components/data/Loading";
import {useEffect, useState} from "react";
import getData from "../../api/backend-api/common/getData";

const Gardener = () => {
  const gardenerId = localStorage.getItem("gardenerId");

  const [isLoading, setLoading] = useState(true);

  // 백엔드에서 받아온 회원 정보
  const [gardener, setGardener] = useState({
    username: "",
    email: "",
    name: "",
    provider: "",
    createDate: ""
  });

  const onMountGardener = async () => {
    const data = await getData(`/gardener/${gardenerId}`);
    setGardener(data);
    setLoading(false);
  }

  useEffect(() => {
    onMountGardener();
  }, [])

  return isLoading ? (
    <Loading/>
    ) : (
      <GardenerDetail gardener={gardener} setGardener={setGardener}/>
    )
}

export default Gardener;
