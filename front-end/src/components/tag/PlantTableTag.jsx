import { Tag } from "antd";
import mediumArray from 'src/utils/dataArray/mediumArray';

const PlantTableTag = ({tags}) => {
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
