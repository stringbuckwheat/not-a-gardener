import {Button, ConfigProvider} from "antd";
import themeGreen from "../../themeGreen";

const GButton = ({className, onClick, size, children}) => {
  return (
    <ConfigProvider theme={themeGreen}>
      <Button
        type="primary"
        className={className}
        onClick={onClick}
        size={size}>
        {children}
      </Button>
    </ConfigProvider>
  )
}

export default GButton
