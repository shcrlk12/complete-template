import styled from "styled-components";

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

export const Select = styled.select`
  border-radius: 12px;
  border: 1px solid ${({ theme }) => theme.colors.tertiary};
  height: 36px;
  width: 100%;
  font-size: ${({ theme }) => theme.fontSize.text};
  color: ${({ theme }) => theme.colors.secondary};
  padding: 0 10px;

  &:focus {
    outline: 1px solid ${({ theme }) => theme.colors.primary};
  }
`;
export const ButtonContainer = styled.div`
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
`;
