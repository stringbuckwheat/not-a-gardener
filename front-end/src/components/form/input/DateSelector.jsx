import getDisabledDate from "../../../utils/function/getDisabledDate";
import dayjs from "dayjs";
import locale from "antd/es/date-picker/locale/ko_KR";
import {DatePicker} from "antd";
import React from "react";

const DateSelector = ({
                        name,
                        style = {width: "100%"},
                        defaultValue = new Date().toISOString().split("T")[0],
                        onChange
                      }) => {
  return (
    <DatePicker
      name={name}
      style={style}
      disabledDate={getDisabledDate}
      format={"YYYY-MM-DD"}
      defaultValue={dayjs(defaultValue, "YYYY-MM-DD")}
      onChange={onChange}
      locale={locale}
    />
  )
}

export default DateSelector;
