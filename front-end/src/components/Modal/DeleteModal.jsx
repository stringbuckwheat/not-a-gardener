import React from 'react'
import {useNavigate} from 'react-router-dom';
import {
  CButton,
  CModal,
  CModalBody,
  CModalFooter,
  CModalHeader,
  CModalTitle,
} from '@coreui/react'
import deleteData from 'src/api/backend-api/common/deleteData';
import {useState} from 'react';
import {DeleteOutlined} from '@ant-design/icons';
import {Tooltip} from 'antd';

const DeleteModal = (props) => {
  const {title, url, path, deleteTooltipMsg} = props;
  const navigate = useNavigate();

  const button = props.button
    ? props.button
    : <Tooltip title={deleteTooltipMsg}>
      <DeleteOutlined
        className="font-size-18 text-grey"
        onClick={() => {
          setVisible(true)
        }}/>
    </Tooltip>

  // 삭제 모달 메시지 만들기

  const isEndWithVowel = (title) => {
    const finalCharCode = title.charCodeAt(title.length - 1);

    if (finalCharCode >= 48 && finalCharCode <= 57) { // 숫자
      return isNumberEndWithVowel(finalCharCode); // 조사 구하기
    } else if (finalCharCode >= 45032 && finalCharCode <= 55203) { // 한글
      return isKoreanEndWithVowel(finalCharCode)
    } else if (
      (finalCharCode >= 65 && finalCharCode <= 90) // 영문 대문자
      || (finalCharCode >= 97 && finalCharCode <= 122) // 소문자
    ) {
      return isEnglishEndWithVowel(finalCharCode)
    } else {
      // 숫자, 한글, 영어에 해당하지 않으면 뒤에서 두 번째거로
      // isEndWithVowel(title.substring(title.length - 2));
    }
  }

  const isNumberEndWithVowel = (finalCharCode) => {
    const number = finalCharCode - 48;
    return (number == 2 || number == 3 || number == 4 || number == 5 || number == 9)
  }

  // 은/는, 을/를 구분 함수...
  const isKoreanEndWithVowel = (finalCharCode) => {
    // 0이면 받침 없고 아니면 받침 있음
    const tmp = (finalCharCode - 44032) % 28;
    return tmp == 0;
  }

  const isEnglishEndWithVowel = (finalCharCode) => {
    const finalCharacter = String.fromCharCode(finalCharCode).toLowerCase();
    return (finalCharacter == "a" || finalCharacter == "e" || finalCharacter == "i" || finalCharacter == "o" || finalCharacter == "u" || finalCharacter == "y")
  }

  const modalTitleMsg = title + (isEndWithVowel(title) ? '를' : '을');
  const modalBodyMsg = title + (isEndWithVowel(title) ? '는' : '은');

  const remove = () => {
    deleteData(url, path);
    closeDeleteModal();
    navigate(url, {replace: true, state: "delete"});
  };

  const [visible, setVisible] = useState(false);
  const closeDeleteModal = () => {
    setVisible(false);
  }

  return (
    <>
      <CModal alignment="center" visible={visible} onClose={props.closeDeleteModal}>
        <CModalHeader>
          <CModalTitle>{modalTitleMsg} 삭제하실 건가요?</CModalTitle>
        </CModalHeader>
        <CModalBody>삭제한 {modalBodyMsg} 복구할 수 없습니다.</CModalBody>
        <CModalFooter>
          <CButton color="link-secondary" onClick={remove}><small>삭제하기</small></CButton>
          <CButton color="success" onClick={closeDeleteModal}>돌아가기</CButton>
        </CModalFooter>
      </CModal>
      <div onClick={() => {
        setVisible(true)
      }}>
        {button}
      </div>
    </>
  )
}

export default DeleteModal;
