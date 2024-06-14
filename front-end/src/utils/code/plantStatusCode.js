const PlantStatusCode = Object.freeze({
  ATTENTION_PLEASE: {
    code: "ATTENTION_PLEASE",
    name: "요주의 식물",
    color: "yellow-inverse",
    type: "warning",
    label: "attention"
  },
  HEAVY_DRINKER: {
    code: "HEAVY_DRINKER",
    name: "헤비 드링커",
    color: "blue",
    type: "info",
    label: "heavyDrinker"
  }
})

export default PlantStatusCode;
