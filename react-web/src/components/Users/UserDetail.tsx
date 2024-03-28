import React from "react";
import { BodyInputContainer, ButtonContainer, InputItem } from "./UserDetail.styled";
import Input from "@components/Input/Input";
import Button from "@components/Button/Button";
import { HeaderContainer } from "@pages/Common.styled";
import styled from "styled-components";

const BorderHeaderContainer = styled(HeaderContainer)`
  border-bottom: 1px solid ${({ theme }) => theme.colors.quaternary};
`;

type UserDetailProps = {
  title: string;
  saveButtonName: string;
  saveButtonOnClick: any;
  cancelButtonOnClick: any;
};
const UserDetail = ({
  title,
  saveButtonName,
  saveButtonOnClick,
  cancelButtonOnClick,
}: UserDetailProps) => {
  return (
    <>
      <BorderHeaderContainer>
        <div>{title}</div>
      </BorderHeaderContainer>
      <BodyInputContainer>
        <InputItem>
          <div>User id</div>
          <Input type="email" placeholder="email" />
        </InputItem>
        <InputItem>
          <div>Password</div>
          <Input type="text" placeholder="password" />
        </InputItem>
        <InputItem>
          <div>Confirm Password</div>
          <Input type="text" placeholder="confirm password" />
        </InputItem>
        <InputItem>
          <div>Name</div>
          <Input type="text" placeholder="name" />
        </InputItem>
        <InputItem>
          <div>Role</div>
          <Input type="text" placeholder="role" />
        </InputItem>
      </BodyInputContainer>
      <ButtonContainer>
        <Button text={saveButtonName} isPrimary={true} width="auto" onClick={saveButtonOnClick} />
        <Button text="Cancel" isPrimary={false} width="auto" onClick={cancelButtonOnClick} />
      </ButtonContainer>
    </>
  );
};

export default UserDetail;
