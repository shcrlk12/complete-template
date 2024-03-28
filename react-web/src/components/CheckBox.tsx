import React from "react";
import styled from "styled-components";

const CheckBoxContainer = styled.div`
  height: 32px;
  display: flex;
  align-items: center;
`;

const CheckBoxInput = styled.input`
  appearance: none;
  height: 18px;
  width: 18px;
  border: 1px solid ${({ theme }) => theme.colors.primary};
  border-radius: 3px;
  cursor: pointer;
  transition: background 300ms;

  &:checked {
    background-color: ${({ theme }) => theme.colors.primary};
    border: none;
  }

  &::before {
    content: "";
    color: transparent;
    display: block;
    width: inherit;
    height: inherit;
    border-radius: inherit;
    border: 0;
    background-color: transparent;
    background-size: contain;
  }

  &:checked::before {
    box-shadow: none;
    background-image: url("data:image/svg+xml,%3Csvg   xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24'%3E %3Cpath d='M15.88 8.29L10 14.17l-1.88-1.88a.996.996 0 1 0-1.41 1.41l2.59 2.59c.39.39 1.02.39 1.41 0L17.3 9.7a.996.996 0 0 0 0-1.41c-.39-.39-1.03-.39-1.42 0z' fill='%23fff'/%3E %3C/svg%3E");
  }
`;

const CheckBoxLabel = styled.label`
  display: flex;
  align-items: center;
  & > span {
    padding-left: 12px;
    font-size: 16px;
  }
`;

const CheckBox = () => {
  return (
    <CheckBoxContainer>
      <CheckBoxLabel>
        <CheckBoxInput type="checkbox" />
        <span>Remember me</span>
      </CheckBoxLabel>
    </CheckBoxContainer>
  );
};

export default CheckBox;
