import { useEffect, useState } from "react";
import onMount from "src/api/service/onMount";
import AddItemButton from "src/components/button/AddItemButton";
import NoItem from "src/components/NoItem";
import ChemicalList from "./ChemicalList";

const Chemical = () => {
    const [chemicalList, setChemicalList] = useState([{}]);

    // on mount
    useEffect(() => {
        onMount("/chemical", setChemicalList);
        console.log("chemicalList", chemicalList);
    }, [])

    return(
        <>
        {
            chemicalList.length == 0
            ? <NoItem 
                title="등록한 비료/살충제가 없어요"
                button={<AddItemButton 
                            addUrl="/chemical/add"
                            size="lg" 
                            title="비료/살충제 추가하기" />} />
            : <ChemicalList chemicalList={chemicalList} />
        }
        </>
    )
}

export default Chemical;