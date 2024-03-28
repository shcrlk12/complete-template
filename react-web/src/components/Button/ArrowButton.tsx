import React from "react";

import { StyledArrowButton } from "./ArrowButton.styled";

const ArrowButton = ({ isLeft = true }) => {
  return (
    <>
      <StyledArrowButton isLeft={isLeft}></StyledArrowButton>
    </>
  );
};

export default ArrowButton;
