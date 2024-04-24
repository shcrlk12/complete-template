import styled from "styled-components";

export const Status = styled.div<{ color: string }>`
  min-width: 45px;
  height: 100%;
  background: ${(props) => props.color};
  border-bottom: 1px dashed ${({ theme }) => theme.colors.black};
`;
