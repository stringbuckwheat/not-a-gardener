import Loading from "../../components/data/Loading";
import {Col, Row} from "antd";
import {useEffect, useState} from "react";
import Repot from "./repot/Repot";
import getData from "../../api/backend-api/common/getData";

const Log = () => {
  console.log("LOG COMPONENT CALLING")
  const [loading, setLoading] = useState(true);

  const [repots, setRepots] = useState([]);

  const onMountLog = async () => {
    const repots = await getData("/repot");
    console.log("repots", repots);
    setRepots(() => repots);
    setLoading(false);
  }

  useEffect(() => {
    onMountLog();
  }, []);


  return loading ? (
    <Loading/>
  ) : (
    <Repot repots={repots} />
  )
}

export default Log;
