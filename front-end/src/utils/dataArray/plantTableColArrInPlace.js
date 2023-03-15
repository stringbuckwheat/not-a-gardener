import { Space, Tag } from 'antd';
import { cilPlant } from '@coreui/icons';
import CIcon from '@coreui/icons-react';
import mediumArray from 'src/utils/dataArray/mediumArray';
import { Link } from 'react-router-dom';

const getColorIdxFromMediumArray = (medium) => {
    for (let i = 0; i < mediumArray.length; i++) {
        if (mediumArray[i].value === medium) {
            return i;
        }
    }
}

const plantTableColArrInPlace = [
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
                <Link to={`/plant/${record.plantNo}`}>
                    <CIcon icon={cilPlant}/>
                </Link>
            </Space>
        )
    }
];

export default plantTableColArrInPlace;