import PlantStatusTags from "./PlantStatusTags";

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
          <PlantStatusTags status={status} />
        }
      </div>
    </>
  )
}

export default PlantTitle;
