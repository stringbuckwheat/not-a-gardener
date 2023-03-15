import { Space } from "antd";
import Search from "./search/Search";
import Sort from "./sort/Sort";

const ListHeader = (props) => {
    const placeList = props.placeList;
    const setPlaceList = props.setPlaceList;

    return (
        <div className="mb-4 d-flex justify-content-end">
            <Space wrap>
                <Search
                    placeList={placeList}
                    setPlaceList={setPlaceList} />
                <Sort
                    placeList={placeList}
                    setPlaceList={setPlaceList} />
            </Space>
        </div>
    )
}

export default ListHeader