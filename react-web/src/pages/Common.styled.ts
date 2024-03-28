import { flexCenter, flexSpaceBetween } from "@components/style/Common";
import styled, { css } from "styled-components";

const CommonContainer = styled.div`
  ${flexCenter};
`;

const CommonInner = styled.div`
  min-width: 920px;
  width: 920px;
  display: flex;
  flex-direction: column;
  padding: 12px 16px;
`;

const HeaderContainer = styled.div`
  ${flexSpaceBetween};
  height: 72px;

  & > div:first-child {
    font-size: 32px;
    font-weight: bold;
  }
`;

const UserSearchContainer = styled.div`
  ${flexCenter};
  height: 60px;
`;

const UserTableContainer = styled.div`
  border-radius: 12px;
  border: 1px solid ${({ theme }) => theme.colors.quaternary};
`;

const UserTable = styled.div``;

export const BodyInputContainer = styled.div`
  padding-top: 20px;
  border-bottom: 1px solid ${({ theme }) => theme.colors.quaternary};
`;

export const InputItem = styled.div`
  display: flex;
  margin-bottom: 20px;
  & div:first-child {
    min-width: 170px;
    display: flex;
    align-items: center;
    flex-direction: row-reverse;
    padding-right: 20px;
    font-size: 15px;
    font-weight: bold;
    text-align: right;
  }
`;

export const ButtonContainer = styled.div`
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
`;

export {
  CommonContainer,
  CommonInner,
  HeaderContainer,
  UserSearchContainer,
  UserTableContainer,
  UserTable,
};
