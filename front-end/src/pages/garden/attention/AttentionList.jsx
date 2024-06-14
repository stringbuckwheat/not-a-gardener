import React, {useState} from "react";
import {useSelector} from "react-redux";
import {Button, Tag} from "antd";
import ClickableTag from "../../../components/tag/basic/ClickableTag";
import {PlusOutlined} from "@ant-design/icons";
import AddAttentionModal from "./AddAttentionModal";
import {useNavigate} from "react-router-dom";

const AttentionList = () => {
  const attentionList = useSelector(state => state.gardens.attentions);

  const [isTagHovered, setIsTagHovered] = useState();
  const [isModalOpened, setIsModalOpened] = useState(false);
  const onMouseEnterTag = () => setIsTagHovered(!isTagHovered);

  const navigate = useNavigate();

  return (
    <>
      <AddAttentionModal
        open={isModalOpened}
        closeModal={() => setIsModalOpened(false)}
      />
      <div style={{display: "flex", justifyContent: "space-between"}}>
        <span style={{fontWeight: "bold"}}>요주의 식물</span>
        <Button onClick={() => setIsModalOpened(true)} type="primary" shape="circle" size={"small"} style={{marginRight: "1rem"}} icon={<PlusOutlined />} />
      </div>
      <div>
        {
          attentionList.length == 0
            ?
            <Tag
              color={isTagHovered ? "gold-inverse" : "gold"}
              onClick={() => setIsModalOpened(true)}
              onMouseEnter={onMouseEnterTag}
              onMouseLeave={onMouseEnterTag}
            >
              {isTagHovered ? " 요주의 식물을 등록할래요 " : "등록된 요주의 식물이 없어요"}
            </Tag>
            :
            attentionList.map((status, index) => {
              return <ClickableTag
                key={status.plantId}
                color={"gold"}
                onClick={() => navigate(`/plant/${status.plantId}`)}
                content={status.plantName}/>
            })
        }
      </div>
    </>)
}

export default AttentionList;
