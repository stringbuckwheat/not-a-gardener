import { useState } from 'react';
import DeleteModal from 'src/components/Modal/DeleteModal';
import { CButton } from '@coreui/react';

const { useLocation } = require("react-router-dom")

const PlaceOne = () => {
    const { state } = useLocation();
    console.log("state", state);

    const [visible, setVisible] = useState(false);
    const closeDeleteModal = () => {
        setVisible(false);
    }

    return (
        <>
            <div>식물등: {state.artifialLight}</div>
            <div>장소 이름: {state.placeName}</div>
            <div>옵션: {state.option}</div>
            <div>식물 개수: {state.plantQuantity}</div>
            <CButton color="dark" onClick={() => setVisible(true)}><small>장소 삭제</small></CButton>
            <DeleteModal 
                visible={visible} 
                deleteItem={state.placeNo} 
                closeDeleteModal={closeDeleteModal} 
                title={"장소"}
                deleteUrl={"/place/" + state.placeNo}/>
        </>
    )
}

export default PlaceOne;