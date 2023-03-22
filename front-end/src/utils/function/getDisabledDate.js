const getDisabledDate = (current) => {
    return current && current.valueOf() > Date.now();
}

export default getDisabledDate;