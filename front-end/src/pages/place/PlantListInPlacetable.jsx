import DefaultTable from "src/components/table/DefaultTable";
import { useParams } from 'react-router-dom';
import ModifyPlantPlaceButton from "src/components/button/ModifyPlantPlaceButton";
import { Space, Tag } from 'antd';
import mediumArray from 'src/utils/dataArray/mediumArray';
import { cilPlant } from '@coreui/icons';
import CIcon from '@coreui/icons-react';
import getPlaceList from "src/utils/function/getPlaceList";
import { useNavigate } from 'react-router-dom';

const PlantListInPlaceTable = (props) => {
    const navigate = useNavigate();
    const plantList = props.list.map(
        (rowData) => ({
            key: rowData.plantNo,
            plantNo: rowData.plantNo,
            plantName: rowData.plantName,
            plantSpecies: rowData.plantSpecies,
            averageWateringPeriod: rowData.averageWateringPeriod,
            tags: [rowData.medium],
            createDate: rowData.createDate
        })
    );

    const placeNo = useParams().placeNo;

    const onClickPlantDetail = async (record) => {
        const placeList = await getPlaceList();
        navigate("/plant/" + record.plantNo, { state: { data: record, placeList: placeList } });
    }

    const getColorIdxFromMediumArray = (medium) => {
        for (let i = 0; i < mediumArray.length; i++) {
            if (mediumArray[i].value === medium) {
                return i;
            }
        }
    }

    const plantTableColumnArray = [
        {
            title: '식물 이름',
            dataIndex: 'plantName',
            key: 'plantName',
        },
        {
            title: '식물 종',
            dataIndex: 'plantSpecies',
            key: 'plantSpecies',
            responsive: ['lg']
        },
        {
            title: '평균 물주기',
            dataIndex: 'averageWateringPeriod',
            key: 'averageWateringPeriod',
            responsive: ['lg']
        },
        {
            title: '태그',
            key: 'tags',
            dataIndex: 'tags',
            responsive: ['lg'],
            render: (_, { tags }) => (
                <>
                    {tags.map((tag) => {
                        const colorArr = ["green", "orange", "volcano", "cyan", "geekblue"];

                        return (
                            <Tag color={colorArr[getColorIdxFromMediumArray(tag)]} key={tag}>
                                {tag}
                            </Tag>
                        );
                    })}
                </>
            ),
        },
        {
            title: '자세히',
            dataIndex: 'plantDetail',
            key: 'plantDetail',
            render: (_, record) => (
                <Space size="middle">
                    <CIcon icon={cilPlant} onClick={() => { onClickPlantDetail(record) }} />
                </Space>
            )
        }
    ];

    return (
        <div className="mt-3">
            <DefaultTable
                path={placeNo}
                columns={plantTableColumnArray}
                list={plantList}
            />
        </div>
    )
}

export default PlantListInPlaceTable