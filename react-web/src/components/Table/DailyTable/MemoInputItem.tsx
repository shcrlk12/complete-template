import Input from "@components/Input/Input";
import TextArea from "@components/Input/TextArea";
import { MemoItem, MemoItemLabel } from "./DailyTable.styled";

type MemoInputItemProps = {
  id: string;
  title: string;
  height?: string;
  isInput?: boolean;
  isTextarea?: boolean;
  text?: string;
};
const MemoInputItem = ({ id, title, height, isInput = false, isTextarea = false, text }: MemoInputItemProps) => {
  return (
    <MemoItem>
      <MemoItemLabel htmlFor={id}>{title}</MemoItemLabel>
      {isInput && <Input id={id} height={height} text={text} />}
      {isTextarea && <TextArea id={id} height={height} text={text} />}
    </MemoItem>
  );
};

export default MemoInputItem;
