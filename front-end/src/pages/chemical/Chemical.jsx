import React, {useEffect, useState} from "react";
import onMount from "src/api/service/onMount";
import AddItemButton from "src/components/button/AddItemButton";
import NoItem from "src/components/NoItem";
import ChemicalList from "./ChemicalList";
import onMountWithLength from "../../api/service/onMountWithLength";
import Loading from "../../components/data/Loading";
import AddPlantButton from "../../components/button/AddPlantButton";
import GardenList from "../garden/GardenList";
import getData from "../../api/backend-api/common/getData";

const Chemical = () => {
  const [isLoading, setLoading] = useState(true);
  const [hasChemical, setHasChemical] = useState(false);
  const [chemicalList, setChemicalList] = useState([{}]);

  const onMountChemical = async () => {
    const data = await getData("/chemical");
    setLoading(false);
    setHasChemical(data.length > 0);
    setChemicalList(data);
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
      button={<AddItemButton
        addUrl="/chemical/add"
        size="lg"
        title="비료/살충제 추가하기"/>}/>
  } else {
    return <ChemicalList chemicalList={chemicalList}/>
  }
}

export default Chemical;
