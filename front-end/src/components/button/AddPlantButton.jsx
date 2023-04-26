import {CButton} from "@coreui/react"

const AddPlantButton = ({size = "lg", className, shape = "rounded-pill", onClick}) => {

  return (
    <CButton
      className={className}
      onClick={onClick}
      color="success"
      size={size}
      variant="outline"
      shape={shape}>
      식물 추가하기
    </CButton>
  )
}

export default AddPlantButton
