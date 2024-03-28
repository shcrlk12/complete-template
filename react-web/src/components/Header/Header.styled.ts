import { flexSpaceBetween } from "@components/style/Common";
import styled from "styled-components";

export const StyledHeader = styled.div`
  height: ${({ theme }) => theme.size.headerHeight}px;
  box-sizing: border-box;
  border-bottom: solid 1px #e5e8eb;
  box-shadow: 1px 1px 1px #c4d8f0;
  padding: 0 20px;
  margin-bottom: ${({ theme }) => theme.size.headerMarginBottom}px;
`;

export const HeaderInner = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
`;

export const LeftHeaderContainer = styled.div`
  display: flex;
`;

export const Logo = styled.img`
  margin-right: 15px;
  width: 105px;
  height: 40.4px;
  cursor: pointer;
`;

export const PageTitle = styled.div`
  font-size: ${({ theme }) => theme.fontSize.title};
  font-weight: bold;
  display: flex;
  align-items: center;
`;

export const RightHeaderContainer = styled.div`
  width: 500px;
  margin-right: 40px;
  ${flexSpaceBetween}
`;
