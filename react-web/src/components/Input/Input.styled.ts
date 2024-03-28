import styled from "styled-components";

export const StyledInput = styled.input`
  color: ${({ theme }) => theme.colors.secondary};
  font-size: 16px;
  border: 1px solid ${({ theme }) => theme.colors.tertiary};
  border-radius: 12px;
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  box-sizing: border-box;
  padding: 0 15px;
  cursor: text;

  &:focus {
    outline: 1px solid ${({ theme }) => theme.colors.primary};
  }

  &::placeholder {
    color: ${({ theme }) => theme.colors.secondary};
    font-size: 16px;
  }
`;

export const StyledSearchInput = styled.div<{ height: number; type: string }>`
  width: 100%;
  height: ${(props) => props.height}px;
  border-radius: ${(props) => props.height / 2}px;
  background-color: ${({ theme }) => theme.colors.quaternary};
  border: none;
  padding: 0 15px;
  display: flex;
  align-items: center;
  .searchIcon {
    background-color: inherit;
    color: ${({ theme }) => theme.colors.secondary};
  }

  input {
    margin-left: 10px;
    font-size: 16px;
    height: 100%;
    width: 100%;
    border: none;
    background-color: inherit;
    color: ${({ theme }) => theme.colors.secondary};

    &::placeholder {
      color: ${({ theme }) => theme.colors.secondary};
      font-size: 16px;
    }
  }
`;

export const StyledTextArea = styled.textarea<{ borderWidth?: number; resize?: string }>`
  border: ${(props) => props.borderWidth}px solid ${({ theme }) => theme.colors.tertiary};
  border-radius: 4px;
  overflow: hidden;
  font-size: 15px;
  padding: 4px 6px;
  resize: ${(props) => props.resize};

  &:focus {
    outline: 1px solid ${({ theme }) => theme.colors.primary};
  }
`;
