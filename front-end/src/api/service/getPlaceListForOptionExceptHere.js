const getPlaceListForOptionExceptHere = (data, placeNo) => {
  return data.filter((place) => place.key != placeNo)
    .map((place) => {
      return (
        {
          label: place.value,
          value: place.key
        }
      )
    })
}

export default getPlaceListForOptionExceptHere;
