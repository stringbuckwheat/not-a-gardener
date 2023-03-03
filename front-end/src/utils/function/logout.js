const LogOut = () => {
    localStorage.clear();
    window.location.replace('/');
}

export default LogOut;