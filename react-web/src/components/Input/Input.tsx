import React, { RefObject, forwardRef } from "react";
import { StyledInput } from "./Input.styled";

type InputProps = {
  id?: string;
  type?: string;
  placeholder?: string;
  width?: string;
  height?: string;
  text?: string;
  name?: string;
  ref?: RefObject<HTMLInputElement>;
  disable?: boolean;
};
const CustomInput = forwardRef<HTMLInputElement, InputProps>(
  ({ id, type = "text", placeholder, width = "100%", height = "36px", text, name, disable }, ref) => {
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
          ref={ref}
          disabled={disable}
        />
      </>
    );
  },
);

export default CustomInput;
