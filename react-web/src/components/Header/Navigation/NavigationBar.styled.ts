import styled from "styled-components";

export const StyledGlobalNavigationBarItem = styled.div`
  position: relative;
  margin-right: 36px;
  cursor: pointer;
`;

export const GNBName = styled.span`
  display: flex;
  align-items: center;
  height: 100%;

  padding: 0 14px;
  border-left: 1px solid ${({ theme }) => theme.colors.secondary};

  * {
    background-color: inherit;
  }
`;

export const StyledGlobalNavigationBarList = styled.div`
  height: 40px;
  margin: 0;
  flex-grow: 1;
  justify-content: flex-end;
  display: flex;
  margin-right: 100px;
`;

export const StyledLocalNavigationBarList = styled.div<{ $display: boolean }>`
  position: absolute;
  top: 40px;
  left: -15px;

  z-index: 9999;
  width: 150px;

  padding: 12px;
  background-color: ${({ theme }) => theme.colors.quaternary};
  border: 1px solid ${({ theme }) => theme.colors.tertiary};
  border-radius: 4px;

  box-sizing: border-box;

  display: ${(props) => (props.$display ? "block" : "none")};

  & * {
    background-color: inherit;
  }
`;

export const LocalNavigationBarInnter = styled.ul`
  display: flex;
  flex-direction: column;
  align-content: space-between;
  margin: 0;

  & > li {
    font-size: 14px;
    height: 26px;
    display: flex;
    align-items: center;
    margin-top: 6px;
    box-sizing: border-box;

    &:first-child {
      margin-top: 0;
    }

    & > a {
      display: flex;
      width: 100%;
      height: 100%;
      align-items: center;
      &:hover {
        font-weight: bold;
        color: ${({ theme }) => theme.colors.secondary};
        border-bottom: 2px solid ${({ theme }) => theme.colors.secondary};
      }
    }
  }
`;
