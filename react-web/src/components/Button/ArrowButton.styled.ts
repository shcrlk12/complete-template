import styled from "styled-components";
import rightArrow from "@images/right-arrow.png";
import leftArrow from "@images/left-arrow.png";

const StyledArrowButton = styled.div<{ isLeft: boolean }>`
  height: 30px;
  width: 30px;
  cursor: pointer;
  border-radius: 15px;
  background-size: 24px;
  background-image: url(${(props) => (props.isLeft ? leftArrow : rightArrow)});
  background-color: ${({ theme }) => theme.colors.primary};
  background-repeat: no-repeat;
  background-position: center;
  &:hover {
    background-color: ${({ theme }) => theme.colors.secondary};
  }
`;

export { StyledArrowButton };
