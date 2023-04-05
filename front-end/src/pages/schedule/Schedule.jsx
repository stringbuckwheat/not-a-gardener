import {CCard, CCardBody, CCardGroup, CContainer} from '@coreui/react'
import Routine from "./routine/Routine";
import {useEffect, useState} from "react";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";

const Schedule = () => {
  const [loading, setLoading] = useState(true);
  const [routines, setRoutines] = useState({});

  const onMountSchedule = async () => {
    const res = await getData("/routine");

    res.todoList.sort((a, b) => {
      if (a.isCompleted > b.isCompleted) return 1;
      if (a.isCompleted < b.isCompleted) return -1;
    });

    setRoutines(res);
    setLoading(false);
  }

  useEffect(() => {
    onMountSchedule();
  })

  if(loading){
    return <Loading />
  }

  return (
    <CContainer>
      <CCardGroup>
        <Routine routines={routines}/>
        <CCard className="p-4">
          <CCardBody>
            <div>목표</div>
          </CCardBody>
        </CCard>
      </CCardGroup>
    </CContainer>
  )
}

export default Schedule;
