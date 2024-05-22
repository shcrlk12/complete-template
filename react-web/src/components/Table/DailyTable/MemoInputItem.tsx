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
  onChange?: any;
};
const MemoInputItem = ({
  id,
  title,
  height,
  isInput = false,
  isTextarea = false,
  text,
  onChange,
}: MemoInputItemProps) => {
  return (
    <MemoItem>
      <MemoItemLabel htmlFor={id}>{title}</MemoItemLabel>
      {isInput && <Input id={id} height={height} text={text} onChange={onChange} />}
      {isTextarea && <TextArea id={id} height={height} text={text} onChange={onChange} />}
    </MemoItem>
  );
};

export default MemoInputItem;
