import AddItemCard from "src/components/card/AddItemCard";
import React, {useState} from "react";
import AddChemical from "./AddChemical";
import {useSelector} from "react-redux";
import {Col, Row} from "antd";
import {
  AlertOutlined,
  BugOutlined,
  CoffeeOutlined,
  EllipsisOutlined,
  ExperimentOutlined,
  RocketOutlined
} from "@ant-design/icons";
import ListItemCard from "../../components/card/ListItemCard";
import Style from "../../components/card/ItemCard.module.scss";

const ChemicalList = () => {
  const chemicals = useSelector(state => state.chemicals.chemicals);
  console.log("chemicals", chemicals);

  const [isAddFormOpened, setIsAddFormOpened] = useState(false);

  const switchAddForm = () => setIsAddFormOpened(!isAddFormOpened);

  const getChemicalsForCard = (chemical) => {
    let color = "";
    let icon = {};

    if (chemical.type === "기본 NPK 비료") {
      color = "#007BFF";
      icon = <RocketOutlined/>;
    } else if (chemical.type === "개화용 비료") {
      color = "#007BFF";
      icon = <ExperimentOutlined/>
    } else if (chemical.type === "미량 원소 비료") {
      color = "green";
      icon = <CoffeeOutlined/>
    } else if (chemical.type === "살충제/농약") {
      color = "red";
      icon = <BugOutlined style={{fontSize: "2.5rem"}}/>;
    } else if (chemical.type === "살균제/농약") {
      color = "black";
      icon = <AlertOutlined/>
    }
      // else if (chemical.type === "천적 방제") {
      //   color = "blue";
      //   icon = <BugOutlined style={{...style, backgroundColor: color}}/>;
    // }
    else {
      color = "purple";
      icon = <EllipsisOutlined/>;
    }

    return {
      color,
      icon,
    }
  }

  return isAddFormOpened ? (
    <AddChemical afterAdd={switchAddForm}/>
  ) : (
    <Row>
      <AddItemCard onClick={switchAddForm} addMsg="새로운 약품 추가"/>
      {
        chemicals.map((chemical) => {
          const chemicalForCard = getChemicalsForCard(chemical);
          return (
            <Col xs={24} sm={12} md={8} lg={6} className={Style.wrapper} style={{marginBottom: "1rem"}}
                 key={chemical.id}>
              <ListItemCard color={chemicalForCard.color}
                            icon={chemicalForCard.icon}
                            link={`/chemical/${chemical.id}`}
                            state={chemical}
                            name={chemical.name}
                            type={chemical.type}
                            detail={`${chemical.period}일에 한 번`}/>
            </Col>
          )
        })
      }
    </Row>
  )
}

export default ChemicalList
