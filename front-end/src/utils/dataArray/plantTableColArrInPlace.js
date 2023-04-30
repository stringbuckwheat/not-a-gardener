import {Link} from "react-router-dom";
import {Popconfirm, Space, Tag} from "antd";
import {DeleteOutlined} from "@ant-design/icons";

const plantTableColArrInPlace = (deletePlant) => {

  return (
    [
      {
        title: '식물 이름',
        dataIndex: 'name',
        key: 'name',
        render: (_, record) =>
          (
            <Link to={`/plant/${record.id}`} className="text-decoration-none">
              {record.name}
            </Link>
          )
      },
      {
        title: '식물 종',
        dataIndex: 'species',
        key: 'species',
        responsive: ['lg']
      },
      {
        title: '최근 물주기',
        dataIndex: 'recentWateringPeriod',
        key: 'recentWateringPeriod',
        responsive: ['lg']
      },
      {
        title: '기타',
        key: 'tags',
        dataIndex: 'tags',
        responsive: ['lg'],
        render: (_, {tags}) => (
          <>
            {tags.map((tag, idx) => (<Tag key={idx}>{tag}</Tag>))}
          </>
        ),
      },
      {
        title: '',
        dataIndex: 'plantDetail',
        key: 'plantDetail',
        render: (_, record) => (
          <Space size="middle">
            <Popconfirm
              title={`${record.name}을 삭제하실 건가요?`}
              discription="삭제한 식물은 복구할 수 없어요"
              okText="네"
              cancelText="아니요"
              onConfirm={() => deletePlant(record.id)}
            >
              <DeleteOutlined/>
            </Popconfirm>
          </Space>
        )
      }
    ]
  )
}

export default plantTableColArrInPlace;
