import Button from "@components/Button/Button";
import { flexCenter, flexSpaceBetween } from "@components/style/Common";
import styled from "styled-components";
import TableRow from "../TableRow";

export const TableContentInner = styled.div`
  display: flex;
  height: 100%;
  position: relative;
`;

export const TableLeftInfo = styled.div`
  flex: 7 0 100px;
  height: 100%;
  overflow: auto;
`;
export const StyledBodyRow = styled(TableRow)`
  flex: 0 0 40px;
  min-height: 40px;
  height: 40px;
  display: flex;
`;

export const StyledButton = styled(Button)`
  margin: 0 20px;
`;

export const TableRightInfo = styled.div`
  position: sticky;
  right: 0;
  flex: 1 0 250px;
  max-width: 400px;

  display: flex;
  flex-direction: column;
  overflow: auto;
`;

export const WindFarmAvailContainer = styled.div`
  padding: 0 8px;
  height: 48px;
  min-height: 48px;
  ${flexSpaceBetween};
  border-bottom: 1px solid ${({ theme }) => theme.colors.tertiary};
  box-shadow: 0px 1px 10px ${({ theme }) => theme.colors.secondary};
  margin-bottom: 2px;
  span {
    color: ${({ theme }) => theme.colors.secondary};
    font-size: ${({ theme }) => theme.fontSize.subTitle};
  }
  strong {
    color: ${({ theme }) => theme.colors.primary};
    font-size: ${({ theme }) => theme.fontSize.subTitle};
    font-weight: bold;
  }
`;

export const AvailMemoContainer = styled.div`
  overflow: auto;
  display: flex;
  flex-direction: column;
  padding: 10px 5px 5px 5px;
  min-height: 150px;
  max-height: 200px;
  background-color: ${({ theme }) => theme.colors.quaternary};
  border-bottom: 4px solid ${({ theme }) => theme.colors.tertiary};

  & * {
    background-color: inherit;
    color: ${({ theme }) => theme.colors.black};
  }

  & > div {
    margin-bottom: 3px;

    & > strong {
      font-weight: bold;
    }
  }
`;

export const AvailStatusListContainer = styled.div``;

export const ButtonContainer = styled.div`
  ${flexCenter}
  padding: 5px;
`;

export const MemoItemsContainer = styled.div`
  padding: 5px 8px;
`;

export const MemoItem = styled.div`
  margin-bottom: 5px;
  display: flex;
  flex-direction: column;
  & label {
    margin-bottom: 5px;
  }
`;

export const MemoItemLabel = styled.label`
  cursor: pointer;
`;

export const StyledAvailStatusItem = styled.div`
  ${flexCenter};
  height: 42px;
  padding: 5px 10px;

  & > div:first-child {
    width: 30px;
    height: 30px;
    border-radius: 5px;
    background-color: ${(props) => props.color};
  }
  & > div:nth-child(2) {
    text-align: center;
    padding: 0 8px;
    flex: 1 0 100px;
    ${flexCenter}
  }

  & > div:last-child {
    padding: 0 8px;
    width: 84px;
    ${flexSpaceBetween}

    & > div:nth-child(2) {
      padding: 3px 5px;
      border-radius: 4px;
      background-color: ${({ theme }) => theme.colors.primary};
      color: ${({ theme }) => theme.colors.textOnPrimary};
      box-shadow: 1px 1px 3px ${({ theme }) => theme.colors.secondary};
    }

    & > div:first-child {
      cursor: pointer;
    }
    & > div:last-child {
      cursor: pointer;
    }
  }
`;

export const StatusBox = styled.div<{ color: string }>`
  width: 30px;
  height: 30px;
  border-radius: 5px;
  background-color: ${(props) => props.color};
`;
