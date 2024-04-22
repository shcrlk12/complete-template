import React from "react";

import { StyledArrowButton } from "./ArrowButton.styled";

type ArrowButtonProps = {
  isLeft: boolean;
  onClick: any;
};
const ArrowButton = ({ isLeft = true, onClick }: ArrowButtonProps) => {
  return (
    <>
      <StyledArrowButton isLeft={isLeft} onClick={onClick}></StyledArrowButton>
    </>
  );
};

export default ArrowButton;
