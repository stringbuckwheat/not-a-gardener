import React, {useEffect, useState} from "react";
import NoItem from "src/components/empty/NoItem";
import ChemicalList from "./ChemicalList";
import Loading from "../../components/data/Loading";
import getData from "../../api/backend-api/common/getData";
import AddChemical from "./AddChemical";
import {useDispatch} from "react-redux";
import ChemicalAction from "../../redux/reducer/chemicals/chemicalAction";

const Chemical = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasChemical, setHasChemical] = useState(false);

  const dispatch = useDispatch();

  const onMountChemical = async () => {
    const data = await getData("/chemical");

    dispatch({type: ChemicalAction.FETCH_CHEMICAL, payload: data});

    setLoading(false);
    setHasChemical(data.length > 0);
  }

  // on mount
  useEffect(() => {
    onMountChemical();
  }, [])

  if (isLoading) {
    return (
      <Loading/>
    )
  } else if (!hasChemical) {
    return <NoItem
      title="등록한 비료/살충제가 없어요"
      buttonSize={"lg"}
      buttonTitle={"비료/살충제 추가하기"}
      addForm={<AddChemical afterAdd={() => setHasChemical(true)}/>}
    />
  } else {
    return <ChemicalList />
  }
}

export default Chemical;
