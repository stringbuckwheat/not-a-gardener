import React, { useEffect, useState } from "react";
import NoItem from "src/components/NoItem";
import AddItemButton from "src/components/button/AddItemButton";
import PlaceList from "./PlaceList";
import getData from "src/api/backend-api/common/getData";

const Place = () => {
    const [hasPlace, setHasPlace] = useState(false);
    const [placeList, setPlaceList] = useState([]);

    const onMount = async () => {
        const res = await getData("/place");

        setHasPlace(res.length > 0)
        setPlaceList(res);
    }

    useEffect(() => {
        onMount();
    }, [])

    return (
        <>
            {
                !hasPlace
                    ? <NoItem title="등록된 장소가 없어요" button={<AddItemButton addUrl="/place/add" size="lg" title="장소 추가하기" />} />
                    : <PlaceList 
                        placeList={placeList} 
                        setPlaceList={setPlaceList} />
            }
        </>
    );
}

export default Place;