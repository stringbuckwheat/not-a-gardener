import updateRoutineState from "../../api/backend-api/updateRoutineState";
import isEndWithVowel from "../../utils/function/isEndWithVowel";
import React from "react";
import GButton from "../button/GButton";
import {Button, Modal} from "antd";
import LinkHoverTag from "../tag/basic/LinkHoverTag";
import {useDispatch} from "react-redux";

const RoutineStateUpdateModal = ({visible, closeModal, routineForModal,}) => {
  if (!routineForModal.content) {
    return;
  }

  const dispatch = useDispatch();

  const confirm = async () => {
    const res = await updateRoutineState(routineForModal.routineId, !routineForModal.isCompleted);
    dispatch({type: 'updateRoutine', payload: res})
    closeModal();
  }

  const getTitle = () => {
    const isEndWithVowelForModal = isEndWithVowel(routineForModal.content);
    console.log("isEndWithVowelForModal", isEndWithVowelForModal);

    return (
      <div align={"middle"}>
        <LinkHoverTag content={routineForModal.content}
                      color={routineForModal.isCompleted ? "gold" : "geekblue"}
                      to={"/schedule"}>
        </LinkHoverTag>
        <span>
          {isEndWithVowelForModal ? '를' : '을'}
          {routineForModal.isCompleted ? " 미완으로 변경할까요?" : " 완료하셨나요?"}
        </span>
      </div>
    )
  }

  return (
    <Modal open={visible} onClose={closeModal} closable={false}
           footer={
             <>
               <GButton color="teal" onClick={confirm}>네</GButton>
               <Button onClick={closeModal}>아니요</Button>
             </>}>
      <h6 style={{marginBottom: "2rem", marginTop: "1rem", display: "flex"}}>{getTitle()}</h6>
    </Modal>
  )
}

export default RoutineStateUpdateModal
