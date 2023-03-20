const searchPlace = (originData, searchWord) => {
    return originData.filter(place => place.placeName.includes(searchWord))
}

export default searchPlace;