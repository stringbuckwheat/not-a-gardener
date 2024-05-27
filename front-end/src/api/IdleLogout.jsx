import React, {useRef} from 'react';
import {useIdleTimer} from 'react-idle-timer';
import logout from "../utils/function/logout";

const IdleLogout = () => {
  const idleTimerRef = useRef(null);

  const onIdle = () => {
    console.log("onIdle");
    if (!localStorage.getItem("accessToken")) {
      return;
    }

    logout().then(() => alert('오랫동안 활동이 없어서 로그아웃됐어요.'));
  }

  useIdleTimer({
    ref: idleTimerRef,
    timeout: 1000 * 60 * 15, // 15분
    onIdle: onIdle,
    debounce: 500
  });

  return null;
};

export default IdleLogout;
