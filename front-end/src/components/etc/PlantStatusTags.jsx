import {Tag} from "antd";
import PlantStatusCode from "../../utils/code/plantStatusCode";

const PlantStatusTags = ({status}) => {
  return (
    <>
      {
        status.attention == "Y" ?
          <Tag color={PlantStatusCode.ATTENTION_PLEASE.color} className={"text-orange"}>
            {PlantStatusCode.ATTENTION_PLEASE.name}
          </Tag>
          : <></>
      }
      {
        status.heavyDrinker == "Y" ?
          <Tag color={PlantStatusCode.HEAVY_DRINKER.color}>
            {PlantStatusCode.HEAVY_DRINKER.name}
          </Tag>
          : <></>
      }
    </>
  )
}

export default PlantStatusTags
