import { useLocation } from 'react-router-dom'
import PlantTag from './PlantTag';
import DetailLayout from 'src/components/form/DetailLayout';

const PlantDetail = () => {
    const { state } = useLocation();
    const title = state.plantName;

    return (
        <DetailLayout
            title={title}
            tags={<PlantTag plant={state}/>}
            bottomData={<div>plantLog 들어갈 자리</div>}
        />
    )
}

export default PlantDetail;