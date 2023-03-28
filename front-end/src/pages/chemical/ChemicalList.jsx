import {CRow} from "@coreui/react";
import AddItemCard from "src/components/card/AddItemCard";
import ChemicalCard from "src/components/card/ChemicalCard";

const ChemicalList = (props) => {
  const chemicalList = props.chemicalList;

  return (
    <>
      {/* <ListHeader data={data} setDat={setData} /> */}
      <CRow>
        <AddItemCard addUrl="/chemical/add" addMsg="비료/살충제 추가"/>
        {chemicalList.map((chemical) => (
          <ChemicalCard chemical={chemical}/>
        ))}
      </CRow>
    </>
  )

}

export default ChemicalList
