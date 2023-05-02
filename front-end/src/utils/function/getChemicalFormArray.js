import chemicalTypeArray from "../dataArray/chemicalTypeArray";

const getChemicalFormArray = (chemical) => {
  console.log("chemical", chemical);
  return ( [
      {
        inputType: "text",
        label: "이름",
        name: "name",
        defaultValue: chemical.name,
        required: true
      },
      {
        inputType: "select",
        label: "종류",
        name: "type",
        defaultValue: chemical.type,
        optionArray: chemicalTypeArray
      },
      {
        inputType: "number",
        label: "주기 (일)",
        name: "period",
        defaultValue: chemical.period,
        required: true
      }
    ]
  )
}

export default getChemicalFormArray
