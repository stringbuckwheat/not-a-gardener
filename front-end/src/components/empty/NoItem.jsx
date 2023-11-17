import React, {useState} from "react";
import {Button} from "antd";
import forNoPlant from '../../assets/images/forNoPlant.png'
import Style from './Empty.module.scss'

/**
 * 데이터 없음 페이지 or 추가하기 폼
 * @param title
 * @param buttonSize
 * @param buttonTitle
 * @param addForm 버튼 클릭 시 보여줄 추가 폼
 * @returns {JSX.Element}
 * @constructor
 */
const NoItem = ({title, buttonSize, buttonTitle, addForm}) => {
  const [isAddFormOpened, setIsAddFormOpened] = useState(false);

  return isAddFormOpened ? (
    <>
      {addForm}
    </>
  ) : (
    <div style={{textAlign: "center"}}>
      <h2 className={Style.title}>{title}</h2>
      <div className={Style.long}>
        <Button
          className={Style.button}
          onClick={() => setIsAddFormOpened(true)}
          size={buttonSize}>
          {buttonTitle}
        </Button>
      </div>
      <img src={forNoPlant}/>
    </div>
  )
}

export default NoItem;
