import React, {useEffect, useState} from "react";
import AddItemButton from "src/components/button/AddItemButton";
import NoItem from "src/components/NoItem";
import ChemicalList from "./ChemicalList";
import Loading from "../../components/data/Loading";
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

  const addChemical = (chemical) => {
    chemicalList.unshift(chemical);
    setChemicalList(chemicalList => chemicalList);
  }

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
    return <ChemicalList chemicalList={chemicalList} addChemical={addChemical}/>
  }
}

export default Chemical;
