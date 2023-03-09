import { Space, Tag } from 'antd';
import mediumArray from 'src/utils/dataArray/mediumArray';
import { DeleteOutlined } from "@ant-design/icons";
import { useState } from 'react';
import { Table } from 'antd';
import { Link } from 'react-router-dom';
import { CLink } from '@coreui/react';
import DeleteModal from 'src/components/modal/DeleteModal';

const PlantTable = (props) => {
    const plantList = props.plantList.map(
        (rowData) => ({
            key: rowData.plantNo,
            plantNo: rowData.plantNo,
            plantName: rowData.plantName,
            plantSpecies: rowData.plantSpecies,
            placeName: rowData.placeName,
            averageWateringPeriod: `${rowData.averageWateringPeriod}일 마다`,
            tags: [rowData.medium, "일째 반려중"], // 함께한 지 며칠째, 물주기 정보 몇 개...
            createDate: rowData.createDate
        })
    );

    const getColorIdxFromMediumArray = (medium) => {
        for (let i = 0; i < mediumArray.length; i++) {
            if (mediumArray[i].value === medium) {
                return i;
            }
        }
    };

    const onClickTableRow = (record) => {
        alert(record + "clicked");
    }

    const plantTableColumnArray = [
        {
            title: '식물 이름',
            dataIndex: 'plantName',
            key: 'plantName',
            render: (_, record) => {
                const link = `/plant/${record.plantNo}`;
                return (
                    <Link to={link} style={{ textDecoration: "none" }}>{record.plantName}</Link>
                )
            }
        },
        {
            title: '식물 종',
            dataIndex: 'plantSpecies',
            key: 'plantSpecies',
            responsive: ['lg']
        },
        {
            title: '장소',
            dataIndex: 'placeName',
            key: 'placeName',
            responsive: ['lg']
        },
        {
            title: '마지막 관수',
            dataIndex: 'latestWateringDay',
            key: 'latestWateringDay',
            responsive: ['lg']
        },
        {
            title: '물주기',
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
            title: '',
            dataIndex: '',
            key: '',
            render: (_, record) => (
                <Space size="middle">
                    <DeleteOutlined onClick={() => { onClickDelete(record) }} />
                </Space>
            )
        }
    ];

    const [ deleteModalData, setDeleteModalData ] = useState({
        plantNo: 0,
        plantName: ""
    })

    const onClickDelete = (record) => {
        setDeleteModalData({
            plantNo: record.plantNo,
            plantName: record.plantName
        })
        setVisible(true);
    }

    // 삭제 모달 용 변수, 함수
    const [visible, setVisible] = useState(false);
    const closeDeleteModal = () => {
        setVisible(false);
    }

    const [selectedRowKeys, setSelectedRowKeys] = useState([]);

    const onSelectChange = (newSelectedRowKeys) => {
        console.log('selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
        console.log("selectedPlantNo", selectedRowKeys);
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    const hasSelected = selectedRowKeys.length > 0;


    return (
        <div className="mt-3">
            {
                hasSelected
                    ?
                    <div>n개의 식물을 삭제, 장소 이동, 물주기 초기화</div>
                    : <></>
            }
            <DeleteModal
                url="/plant"
                path={deleteModalData.plantNo}
                title={deleteModalData.plantName}
                visible={visible}
                closeDeleteModal={closeDeleteModal}
            />
            <Table
                className="mt-3"
                columns={plantTableColumnArray}
                dataSource={plantList}
                rowSelection={rowSelection} // for multi-select
            />
        </div>
    )
}

export default PlantTable;