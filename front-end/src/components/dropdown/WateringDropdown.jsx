import { Button, Dropdown } from "antd";
import authAxios from "src/utils/interceptors";
import dayjs from "dayjs"

const WateringDropdown = (props) => {
    const plantNo = props.plantNo;
    const fertilizerList = props.fertilizerList;
    const setPlantList = props.setPlantList;
    const openNotification = props.openNotification;

    const today = dayjs().format("YYYY-MM-DD");

    const onClick = ({ key }) => {
        console.log(`클릭: ${key}`)

        if (key === "notDry") {
            sendNotDry();
        } else {
            addWatering(key);
        }
    };

    const addWatering = (key) => {
        console.log(`${plantNo}번 식물 물을 줬어요. fertilizerNo: ${key}`);
        console.log("오늘 날짜", today);

        const data = {
            plantNo: plantNo,
            fertilizerNo: key,
            wateringDate: today
        }

        authAxios.post("/watering", data)
            .then((res) => {
                console.log("물주기 Res", res.data);
                openNotification(res.data.wateringMsg);

                authAxios.get("/garden")
                    .then((res) => {
                        res.data.sort((a, b) => {
                            return a.wateringCode - b.wateringCode;
                        })

                        setPlantList(res.data);
                    })
            })
    }

    const sendNotDry = () => {
        console.log("안 마름", plantNo);

        authAxios.put(`/plant/${plantNo}/averageWateringPeriod`)
            .then((res) => {
                console.log("res", res);

                authAxios.get("/garden")
                    .then((res) => {
                        res.data.sort((a, b) => {
                            return a.wateringCode - b.wateringCode;
                        })

                        setPlantList(res.data);
                    })
            })
    }

    const items = [
        {
            key: '0',
            label: "물을 줬어요",
        },
        {
            key: 'fertilizer',
            label: "비료를 줬어요",
            children: fertilizerList
        },
        {
            key: 'notDry',
            label: "화분이 마르지 않았어요",
        },
    ]

    return (
        <Dropdown
            menu={{ items, onClick }}
            className="float-end"
            placement="bottomRight"
            arrow>
            <div>
                <Button type="link">물 주기/미루기</Button>
            </div>
        </Dropdown>
    )
}

export default WateringDropdown;