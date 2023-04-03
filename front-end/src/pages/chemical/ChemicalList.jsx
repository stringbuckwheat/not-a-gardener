import {CRow} from "@coreui/react";
import AddItemCard from "src/components/card/AddItemCard";
import ChemicalCard from "src/components/card/ChemicalCard";
import {useState} from "react";
import AddChemical from "./AddChemical";

const ChemicalList = ({chemicalList, addChemical}) => {
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);

  const switchAddForm = () => setIsAddFormOpened(!isAddFormOpened);

  return isAddFormOpened ? (
    <AddChemical addChemical={addChemical} closeAddForm={switchAddForm}/>
  ) :(
    <>
      <CRow>
        <AddItemCard onClick={switchAddForm} addUrl="/chemical/add" addMsg="비료/살충제 추가"/>
        {chemicalList.map((chemical) => (
          <ChemicalCard chemical={chemical}/>
        ))}
      </CRow>
    </>
  )

}

export default ChemicalList
