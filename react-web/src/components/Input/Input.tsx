import React from "react";
import { StyledInput } from "./Input.styled";

type InputProps = {
  id?: string;
  type?: string;
  placeholder?: string;
  width?: string;
  height?: string;
  text?: string;
  name?: string;
};
const Input = ({ id, type = "text", placeholder, width = "100%", height = "36px", text, name }: InputProps) => {
  return (
    <>
      <StyledInput
        id={id}
        type={type}
        placeholder={placeholder}
        width={width}
        height={height}
        value={text}
        name={name}
      />
    </>
  );
};

export default Input;
