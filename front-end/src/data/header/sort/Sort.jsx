import { Select } from "antd";

const Sort = (props) => {
    const placeList = props.placeList;
    const setPlaceList = props.setPlaceList;

    const sortPlaceList = (value) => {
        const copyPlaceList = [...placeList];

        if (value === "abc") {
            copyPlaceList.sort((a, b) => (a.placeName < b.placeName ? -1 : a.placeName > b.placeName ? 1 : 0))
        } else if (value === "manyPlant") {
            copyPlaceList.sort((a, b) => b.plantListSize - a.plantListSize)
        } else if (value === "createDate") {
            copyPlaceList.sort((a, b) => new Date(a.createDate) - new Date(b.createDate))
        } else if (value === "createDateDesc") {
            copyPlaceList.sort((a, b) => new Date(b.createDate) - new Date(a.createDate))
        }

        setPlaceList(copyPlaceList);
    };

    const options = [
        { value: 'abc', label: '가나다순' },
        { value: 'manyPlant', label: '식물 많은순' },
        { value: 'createDate', label: '생성일순' },
        { value: 'createDateDesc', label: '최근 생성순' },
    ]

    return (
        <Select
            defaultValue="createDate"
            style={{ width: 130 }}
            onChange={sortPlaceList}
            options={options}
        />
    )
}

export default Sort;