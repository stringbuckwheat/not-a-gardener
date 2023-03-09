import { useParams } from 'react-router-dom'
import PlantTag from './PlantTag';
import DetailLayout from 'src/components/form/DetailLayout';
import authAxios from 'src/utils/interceptors';
import { useState, useEffect } from 'react';
import ModifyPlant from './ModifyPlant';
import getPlaceList from 'src/utils/function/getPlaceList';

const PlantDetail = () => {
    const plantNo = useParams().plantNo;
    console.log("plantNo", plantNo);
    const [plant, setPlant] = useState({})

    useEffect(() => {
        authAxios.get(`/plant/${plantNo}`)
            .then((res) => {
                console.log("plant detail response", res.data);
                setPlant(res.data);
            })
            .catch((error) => {
                console.log("error", error);
            })
    }, [])

    const [ onModify, setOnModify ] = useState(false);
    const [ placeList, setPlaceList ] = useState({});
    const onClickModifyBtn = async () => {
        const places = await getPlaceList();
        console.log("places", places);
        setPlaceList(places);
        setOnModify(!onModify);
        console.log("onclick end");
    }

    return (
        !onModify
            ?
            <DetailLayout
                title={plant.plantName}
                url="/plant"
                path={plant.plantNo}
                deleteTitle="식물"
                tags={<PlantTag plant={plant} />}
                onClickModifyBtn={onClickModifyBtn}
                bottomData={<div>plantLog 들어갈 자리</div>}
            />
            :
            <ModifyPlant
                title={plant.plantName}
                plant={plant}
                placeList={placeList}
                onClickGetBackBtn={onClickModifyBtn}
            />
    )
}

export default PlantDetail;