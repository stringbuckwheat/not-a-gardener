import chemicalTypeArray from "../dataArray/chemicalTypeArray";

const getChemicalFormArray = (chemical) => {
  return ( [
      {
        inputType: "text",
        label: "이름",
        name: "chemicalName",
        defaultValue: chemical.chemicalName,
        required: true
      },
      {
        inputType: "select",
        label: "종류",
        name: "chemicalType",
        defaultValue: chemicalTypeArray[0].value,
        optionArray: chemicalTypeArray
      },
      {
        inputType: "number",
        label: "주기 (일)",
        name: "chemicalPeriod",
        defaultValue: chemical.chemicalPeriod,
        required: true
      }
    ]
  )
}

export default getChemicalFormArray
