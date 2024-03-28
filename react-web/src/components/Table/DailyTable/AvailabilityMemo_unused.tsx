import React from "react";
import { MemoItem } from "./DailyTable.styled";

type AvailabilityMemoProps = {
  name: string;
  material: string;
  quantity: string;
  action: string;
  enviromental: string;
  repair: string;
};

const AvailabilityMemo = ({ name, material, quantity, action, enviromental, repair }: AvailabilityMemoProps) => {
  return (
    <>
      <MemoItem>
        <strong>엔지니어 이름:</strong> {name}
      </MemoItem>
      <MemoItem>
        <strong>자재 :</strong> {material}
      </MemoItem>
      <MemoItem>
        <strong>수량 :</strong> {quantity}
      </MemoItem>
      <MemoItem>
        <strong>조치내역:</strong> {action}
      </MemoItem>
      <MemoItem>
        <strong>환경적 요인:</strong> {enviromental}
      </MemoItem>
      <MemoItem>
        <strong>고장 수리 :</strong> {repair}
      </MemoItem>
    </>
  );
};

export default AvailabilityMemo;
