import { Tag } from "antd";
import mediumArray from 'src/utils/dataArray/mediumArray';

const PlantTableTag = (props) => {
    const tags = props.tags;
    console.log("plant table tag", tags);

    const getColorIdxFromMediumArray = (medium) => {
        for (let i = 0; i < mediumArray.length; i++) {
            if (mediumArray[i].value === medium) {
                return i;
            }
        }
    };

    const colorArr = ["green", "orange", "volcano", "cyan", "geekblue"];

    return (
        <>
            <Tag color={colorArr[getColorIdxFromMediumArray(tags.medium)]} key={tags.medium}>
                                        {tags.medium}
                        </Tag>
        </>
    )

}

export default PlantTableTag;

// (_, { tags }) => {
//     console.log("tags", tags);
//     console.log("_", _);

//     
// },