import {Tag} from "antd";
import PlantStatusCode from "../../utils/code/plantStatusCode";

const PlantTitle = ({name, species, status}) => {
  const style = {
    fontSize: "0.9rem",
    fontWeight: "lighter",
    fontStyle: "italic",
    marginLeft: "0.5rem"
  };


  return (
    <>
      {name}
      {
        species
          ? <span style={style}>_ {species}</span>
          : <></>
      }
      <div>
        {
          status?.filter(status => status.active == "Y")
            .map((status) =>
              <Tag key={status.statusId}
                   color={PlantStatusCode[status.status].color}
                   className={PlantStatusCode.ATTENTION_PLEASE.code == status.status ? "text-orange" : ""}>
                {PlantStatusCode[status.status].name}
              </Tag>
            )
        }
      </div>
    </>
  )
}

export default PlantTitle;
