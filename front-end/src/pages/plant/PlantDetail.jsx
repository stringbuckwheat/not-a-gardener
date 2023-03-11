import { useLocation, useParams } from 'react-router-dom'
import PlantTag from './PlantTag';
import DetailLayout from 'src/components/form/DetailLayout';
import authAxios from 'src/utils/interceptors';
import { useState, useEffect } from 'react';
import ModifyPlant from './ModifyPlant';
import getPlaceList from 'src/utils/function/getPlaceList';
import WateringList from '../watering/WateringList';

const PlantDetail = () => {
    const plantNo = useParams().plantNo;
    console.log("plantNo", plantNo);

    const { state } = useLocation();

    const [ plant, setPlant ] = useState({});
    const [ wateringList, setWateringList ] = useState([{}]);

    useEffect(() => {
        authAxios.get(`/plant/${plantNo}`)
            .then((res) => {
                console.log("plant detail response", res.data);
                setPlant(res.data);
            })

        authAxios.get(`/plant/${plantNo}/watering`)
            .then((res) => {
                setWateringList(res.data);
            })
    }, [])

    useEffect(() => {
        if (state != null) {
            setPlant(state);
        }
    }, [state])

    const [onModify, setOnModify] = useState(false);
    const [placeList, setPlaceList] = useState({});
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
                tags={<PlantTag plant={plant} latestWateringDate={wateringList[0]}/>}
                onClickModifyBtn={onClickModifyBtn}
                bottomData={<WateringList plant={plant} wateringList={wateringList} />}
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