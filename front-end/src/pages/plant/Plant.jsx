import {useEffect, useState} from "react";
import PlantList from "./PlantList";
import getData from "../../api/backend-api/common/getData";
import Loading from "../../components/data/Loading";
import NoItemForPlant from "../../components/empty/NoItemForPlant";
import {useDispatch} from "react-redux";

const Plant = () => {
  const dispatch = useDispatch();

  const [isLoading, setLoading] = useState(true);
  const [hasPlant, setHasPlant] = useState(false);

  const onMountPlant = async () => {
    const data = await getData("/garden/plants");
    dispatch({type: 'setPlants', payload: data});
    setLoading(false);
    setHasPlant(data.length > 0);
  }

  useEffect(() => {
    onMountPlant();
  }, [])

  if (isLoading) {
    return <Loading/>
  } else if (!hasPlant) {
    return <NoItemForPlant
      afterAdd={() => setHasPlant(true)}
    />
  } else {
    return <PlantList />
  }
}

export default Plant;
