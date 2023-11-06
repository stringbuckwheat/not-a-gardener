import updateRoutineState from "../../api/backend-api/updateRoutineState";
import {CModal, CModalBody} from "@coreui/react";
import isEndWithVowel from "../../utils/function/isEndWithVowel";
import React from "react";
import GButton from "../button/GButton";
import {Button, Space} from "antd";
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
    <CModal alignment="center" visible={visible} onClose={closeModal}>
      <CModalBody>
        <h6 className="mb-4 mt-2 d-flex">{getTitle()}</h6>
        <div>
          <Space className="float-end">
            <GButton color="teal" onClick={confirm}>네</GButton>
            <Button onClick={closeModal}>아니요</Button>
          </Space>
        </div>
      </CModalBody>
    </CModal>
  )
}

export default RoutineStateUpdateModal
