import { flexCenter } from "@components/style/Common";
import React from "react";
import styled from "styled-components";

const StyledContainer = styled.div<{ width: string }>`
  width: ${(props) => props.width};
`;

const StyledInner = styled.div`
  ${flexCenter};
`;

const RectStatus = styled.div<{ cubeColor: string }>`
  width: 30px;
  height: 30px;
  background-color: ${(props) => props.cubeColor};
  border-radius: 5px;
  margin-right: 10px;
`;

type PowerConditionProps = {
  cubeColor: string;
  width: string;
  text: string;
};
const PowerCondition = ({ cubeColor = "#339d33", width = "200px", text }: PowerConditionProps) => {
  return (
    <StyledContainer width={width}>
      <StyledInner>
        <RectStatus cubeColor={cubeColor}></RectStatus>
        <div>{text}</div>
      </StyledInner>
    </StyledContainer>
  );
};

export default PowerCondition;
