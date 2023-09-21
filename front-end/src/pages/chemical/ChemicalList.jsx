import {CRow} from "@coreui/react";
import AddItemCard from "src/components/card/AddItemCard";
import ChemicalCard from "src/pages/chemical/ChemicalCard";
import {useState} from "react";
import AddChemical from "./AddChemical";
import {useSelector} from "react-redux";

const ChemicalList = () => {
  const chemicals = useSelector(state => state.chemicals.chemicals);
  console.log("chemicals", chemicals);

  const [isAddFormOpened, setIsAddFormOpened] = useState(false);

  const switchAddForm = () => setIsAddFormOpened(!isAddFormOpened);

  return isAddFormOpened ? (
    <AddChemical afterAdd={switchAddForm}/>
  ) : (
    <>
      <CRow>
        <AddItemCard onClick={switchAddForm} addMsg="비료/살충제 추가"/>
        {chemicals.map((chemical) => (
          <ChemicalCard chemical={chemical} key={chemical.id}/>
        ))}
      </CRow>
    </>
  )

}

export default ChemicalList
