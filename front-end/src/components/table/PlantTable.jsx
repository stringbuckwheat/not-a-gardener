import { Space, Tag, Table, Popconfirm } from 'antd';
import mediumArray from 'src/utils/dataArray/mediumArray';
import { DeleteOutlined } from "@ant-design/icons";
import { Link } from 'react-router-dom';
import getPlantListForPlantTable from 'src/api/service/getPlantListForPlantTable';
import deleteData from 'src/api/backend-api/common/deleteData';

const PlantTable = (props) => {
    const originPlantList = props.plantList;
    const setPlantList = props.setPlantList;
    const placeList = props.placeList;

    const plantList = getPlantListForPlantTable(originPlantList);

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
                    <Link 
                        to={link} 
                        className="no-text-decoration">
                            {record.plantName}
                    </Link>
                )
            },
            sorter: (a, b) => {
                if(a.plantName > b.plantName) return 1;
                if(a.plantName === b.plantName) return 0;
                if(a.plantName < b.plantName) return -1;
            },
            sortDirection: ['descend'],
        },
        {
            title: '식물 종',
            dataIndex: 'plantSpecies',
            key: 'plantSpecies',
            responsive: ['lg'],
            sorter: (a, b) => {
                if(a.plantSpecies > b.plantSpecies) return 1;
                if(a.plantSpecies === b.plantSpecies) return 0;
                if(a.plantSpecies < b.plantSpecies) return -1;
            },
            sortDirection: ['descend']
        },
        {
            title: '장소',
            dataIndex: 'placeName',
            key: 'placeName',
            responsive: ['lg'],
            filters: placeList.map((place) => ({
                text: place.placeName,
                value: place.placeName
            })),
            onFilter: (value, record) => record.placeName.indexOf(value) == 0,
        },
        {
            title: '마지막 관수',
            dataIndex: 'latestWateringDate',
            key: 'latestWateringDate',
            responsive: ['lg'],
            sorter: (a, b) => {
                if(a.latestWateringDate > b.latestWateringDate) return 1;
                if(a.latestWateringDate === b.latestWateringDate) return 0;
                if(a.latestWateringDate < b.latestWateringDate) return -1;
            },
            sortDirection: ['descend']
        },
        {
            title: '물주기',
            dataIndex: 'averageWateringPeriod',
            key: 'averageWateringPeriod',
            responsive: ['lg'],
            sorter: (a, b) => {
                if(a.averageWateringPeriod > b.averageWateringPeriod) return 1;
                if(a.averageWateringPeriod === b.averageWateringPeriod) return 0;
                if(a.averageWateringPeriod < b.averageWateringPeriod) return -1;
            },
            sortDirection: ['descend']
        },
        {
            title: '태그',
            key: 'tags',
            dataIndex: 'tags',
            responsive: ['lg'],
            render: (tags) => {
                console.log("tags", tags);
                const colorArr = ["green", "orange", "volcano", "cyan", "geekblue"];

                return (
                    <>
                        <Space size={[0, 8]} wrap>
                            <Tag color={colorArr[getColorIdxFromMediumArray(tags.medium)]}>{tags.medium}</Tag>
                            <Tag color="blue">{tags.createDate}</Tag>
                            {
                                tags.anniversary
                                    ? <Tag color="purple">{tags.anniversary}</Tag>
                                    : <></>
                            }
                            {
                                tags.birthday
                                    ? <Tag color="cyan">birthday {tags.birthday}</Tag>
                                    : <></>
                            }
                        </Space>
                    </>
                )
            },
        },
        {
            title: '',
            dataIndex: '',
            key: '',
            render: (_, record) => (
                <Space size="middle">
                    <Popconfirm
                        placement="topRight"
                        title={`'${record.plantName}'을/를 삭제하실 건가요?`}
                        description="삭제한 식물 정보는 복구할 수 없어요"
                        onConfirm={() => { confirm(record.plantNo) }}
                        okText="네"
                        cancelText="아니요"
                    >
                        <DeleteOutlined />
                    </Popconfirm>
                </Space>
            )
        }
    ];

    const confirm = (plantNo) => {
        deleteData("/plant", plantNo);
        const afterDelete = props.plantList.filter(plant => plant.plantNo !== plantNo);
        setPlantList(afterDelete);
    };


    // const [deleteModalData, setDeleteModalData] = useState({
    //     plantNo: 0,
    //     plantName: ""
    // })

    // const onClickDelete = (record) => {
    //     setDeleteModalData({
    //         plantNo: record.plantNo,
    //         plantName: record.plantName
    //     })
    // }

    // const [selectedRowKeys, setSelectedRowKeys] = useState([]);

    // const onSelectChange = (newSelectedRowKeys) => {
    //     console.log('selectedRowKeys changed: ', newSelectedRowKeys);
    //     setSelectedRowKeys(newSelectedRowKeys);
    //     console.log("selectedPlantNo", selectedRowKeys);
    // };

    // const rowSelection = {
    //     selectedRowKeys,
    //     onChange: onSelectChange,
    // };

    // const hasSelected = selectedRowKeys.length > 0;

    const locale = {
        triggerDesc: '내림차순으로 보기',
        triggerAsc: '오름차순으로 보기', 
        cancelSort: '정렬 취소하기'
    }

    return (
        <div className="mt-3">
            {/* {
                hasSelected
                    ?
                    <div>n개의 식물을 삭제, 장소 이동, 물주기 초기화</div>
                    : <></>
            } */}
            <Table
                className="mt-3"
                locale={locale}
                columns={plantTableColumnArray}
                dataSource={plantList}
            // rowSelection={rowSelection} // for multi-select
            />
        </div>
    )
}

export default PlantTable;