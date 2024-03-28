import React, { useRef } from "react";
import { StyledTextArea } from "./Input.styled";

type TextAreaProps = {
  id?: string;
  cols?: number;
  rows?: number;
  borderWidth?: number;
  height?: string;
  resize?: "vertical" | "horizontal" | "both" | "none";
  text?: string;
};

const TextArea = ({ id, cols = 50, rows = 1, borderWidth = 2, height, resize = "vertical", text }: TextAreaProps) => {
  const textAreaRef = useRef<HTMLTextAreaElement>(null);

  return (
    <StyledTextArea
      id={id}
      onInput={() => {
        if (textAreaRef.current !== null) {
          textAreaRef.current.style.height = textAreaRef.current.scrollHeight + 3 + "px";
        }
      }}
      ref={textAreaRef}
      resize={resize}
      borderWidth={borderWidth}
      cols={cols}
      rows={rows}
      value={text}></StyledTextArea>
  );
};

export default TextArea;
