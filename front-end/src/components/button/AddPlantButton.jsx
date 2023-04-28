import {CButton} from "@coreui/react"

// TODO garden noItem 로직 수정 후 삭제
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
