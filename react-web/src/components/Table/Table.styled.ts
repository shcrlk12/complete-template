import styled from "styled-components";

export const Status = styled.div`
  min-width: 45px;
  height: 100%;
  background-color: #339d33;
  border-bottom: 1px dashed ${({ theme }) => theme.colors.black};
`;
