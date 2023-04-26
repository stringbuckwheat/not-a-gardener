import React, {useEffect, useState} from "react";
import NoItem from "src/components/empty/NoItem";
import ChemicalList from "./ChemicalList";
import Loading from "../../components/data/Loading";
import getData from "../../api/backend-api/common/getData";
import AddChemical from "./AddChemical";

const Chemical = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasChemical, setHasChemical] = useState(false);
  const [chemicalList, setChemicalList] = useState([]);

  const onMountChemical = async () => {
    console.log("chemical mount");
    const data = await getData("/chemical");
    console.log("data", data);
    setLoading(false);
    setHasChemical(data.length > 0);
    setChemicalList(data);
  }

  // on mount
  useEffect(() => {
    onMountChemical();
  }, [])

  const addChemical = (chemical) => {
    chemicalList.unshift(chemical);
    setChemicalList(() => chemicalList);
    console.log("chemicalList", chemicalList);
  }

  if (isLoading) {
    return (
      <Loading/>
    )
  } else if (!hasChemical) {
    return <NoItem
      title="등록한 비료/살충제가 없어요"
      buttonSize={"lg"}
      buttonTitle={"비료/살충제 추가하기"}
      addForm={<AddChemical addChemical={addChemical} afterAdd={() => setHasChemical(true)}/>}
    />
  } else {
    return <ChemicalList chemicalList={chemicalList} addChemical={addChemical}/>
  }
}

export default Chemical;
