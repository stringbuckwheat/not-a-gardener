import { useEffect, useState } from "react";
import AddPesticideButton from "src/components/button/AddPesticideButton";
import NoItem from "src/components/NoItem";
import authAxios from "src/utils/interceptors";
import PesticideCard from "src/components/card/PesticideCard";
import { CContainer, CRow, CCol, CWidgetStatsF } from "@coreui/react";
import CIcon from '@coreui/icons-react';
import { useNavigate } from "react-router-dom";
import { cilPlus } from "@coreui/icons";

const Pesticide = () => {
    const [hasPesticide, setHasPesticide] = useState(false);
    const [pesticideList, setPestcideList] = useState([{
        pesticideNo: 0,
        pesticideName: "",
        pesticidePeriod: 0
    }])

    const navigate = useNavigate();

    useEffect(() => {
        authAxios.get("/pesticide")
            .then((res) => {
                console.log("res", res);

                if (res.data.length !== 0) {
                    setHasPesticide(true);
                    setPestcideList(res.data);
                }
            })
    }, [])

    return (
        !hasPesticide
            ? <NoItem title="등록된 살충제가 없어요" button={<AddPesticideButton />} />
            : 
            <CContainer fluid>
            <CRow>
                <CCol md={3} xs={12}>
                    <CWidgetStatsF
                        className="mb-3"
                        color="dark"
                        icon={<CIcon icon={cilPlus} height={30} />}
                        value="살충/살균제 추가"
                        onClick={() => {navigate("/pesticide/add")}}
                    />
                </CCol>

                {/* 카드 컴포넌트 반복 */}
                {pesticideList.map((pesticide) => 
                    <PesticideCard pesticide={pesticide} />
                )}
            </CRow>
        </CContainer>
    )
}

export default Pesticide;