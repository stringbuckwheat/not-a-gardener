import { useLocation } from 'react-router-dom'
import DetailLayout from "src/components/form/DetailLayout";
import PesticideTag from './PesticideTag';

const PesticideDetail = (props) => {
    const { state } = useLocation();
    console.log("state", state);
    const title = state.pesticideName;

    return (
        <DetailLayout
            title={title}
            tags={<PesticideTag pesticide={state}/>}
            bottomData={<div>방제 내역 자리</div>}
        />
    )
}

export default PesticideDetail;