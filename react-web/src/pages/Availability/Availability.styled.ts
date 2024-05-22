import { flexSpaceBetween } from "@components/style/Common";
import styled from "styled-components";

const MainSection = styled.div`
  width: 100%;

  height: calc(100% - ${({ theme }) => theme.size.headerHeight + theme.size.headerMarginBottom}px);
  position: absolute;
  display: flex;
  flex-direction: column;
`;
const TableMetaContainer = styled.div`
  padding: 0 20px;
  margin-top: 10px;
  flex: 0 0 44px;
  max-height: 44px;
  ${flexSpaceBetween};
`;

const AvailabilityTableContainer = styled.div`
  height: calc(100%);
  padding: 15px 25px 10px 25px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow: hidden;
`;

const WindfarmInfoList = styled.ul`
  display: flex;
  width: 550px;
  & > li {
    text-align: center;
    font-size: 17px;
    line-height: 17px;
    flex-grow: 1;
  }

  & div {
    margin-top: 10px;

    & strong {
      font-weight: bold;
      color: ${({ theme }) => theme.colors.secondary};
    }
  }

  & strong {
    font-weight: bold;
  }
`;

const PowerStatusList = styled.div`
  width: 800px;
  display: flex;
  justify-content: space-between;
`;

const Body = styled.div`
  height: 100%;
  position: relative;
`;

export { MainSection, AvailabilityTableContainer, TableMetaContainer, WindfarmInfoList, PowerStatusList, Body };
