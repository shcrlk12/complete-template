import styled from "styled-components";

const StyledButton = styled.button<{
  isPrimary?: boolean;
  isDangerous?: boolean;
  fontWeight?: number;
  width?: string;
}>`
  background-color: ${(props) =>
    props.isPrimary
      ? props.theme.colors.primary
      : props.isDangerous
        ? props.theme.colors.activeDangerous
        : props.theme.colors.quaternary};
  border: none;
  border-radius: 20px;
  height: 40px;
  width: ${(props) => props.width};
  color: ${(props) =>
    props.isPrimary || props.isDangerous ? props.theme.colors.textOnPrimary : props.theme.colors.black};
  font-size: 14px;
  padding: 5px 20px;
  box-shadow: 1px 1px 5px ${({ theme }) => theme.colors.secondary};

  cursor: pointer;
  font-weight: ${(props) => props.fontWeight};
  &:hover {
    background-color: ${(props) =>
      props.isPrimary
        ? props.theme.colors.secondary
        : props.isDangerous
          ? props.theme.colors.dangerous
          : props.theme.colors.tertiary};
  }
`;

export { StyledButton };
