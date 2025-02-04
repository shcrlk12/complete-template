import { useEffect, useRef } from "react";
import { BodyInputContainer, ButtonContainer, InputItem, Select } from "./UserDetail.styled";
import CustomInput from "@components/Input/Input";
import Button from "@components/Button/Button";
import { HeaderContainer } from "@pages/Common.styled";
import styled from "styled-components";
import {
  ROLE_ADMIN,
  ROLE_ANONYMOUS,
  ROLE_MANAGER,
  ROLE_USER,
  User,
  UserRoleType,
  userTypeInitialize,
} from "@reducers/userActions";

const BorderHeaderContainer = styled(HeaderContainer)`
  border-bottom: 1px solid ${({ theme }) => theme.colors.quaternary};
`;

type UserDetailProps = {
  title: string;
  isIdDisable?: boolean;
  userDetail?: User;
  saveButtonName: string;
  saveButtonOnClick: (user: User) => void;
  cancelButtonOnClick: any;
};
const UserDetail = ({
  title,
  isIdDisable,
  userDetail = userTypeInitialize,
  saveButtonName,
  saveButtonOnClick,
  cancelButtonOnClick,
}: UserDetailProps) => {
  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);
  const confirmPasswordRef = useRef<HTMLInputElement>(null);
  const nameRef = useRef<HTMLInputElement>(null);
  const roleRef = useRef<HTMLSelectElement>(null);

  useEffect(() => {
    if (emailRef.current && nameRef.current && roleRef.current) {
      emailRef.current.value = userDetail.id;
      nameRef.current.value = userDetail.name;
      roleRef.current.value = userDetail.role;
    }
  }, [userDetail]);

  return (
    <>
      <BorderHeaderContainer>
        <div>{title}</div>
      </BorderHeaderContainer>
      <BodyInputContainer>
        <InputItem>
          <div>User id</div>
          <CustomInput type="email" placeholder="email" ref={emailRef} disable={isIdDisable} />
        </InputItem>
        <InputItem>
          <div>Password</div>
          <CustomInput type="password" placeholder="password" ref={passwordRef} />
        </InputItem>
        <InputItem>
          <div>Confirm Password</div>
          <CustomInput type="password" placeholder="confirm password" ref={confirmPasswordRef} />
        </InputItem>
        <InputItem>
          <div>Name</div>
          <CustomInput type="text" placeholder="name" ref={nameRef} />
        </InputItem>
        <InputItem>
          <div>Role</div>
          <Select ref={roleRef}>
            <option value={ROLE_USER} selected>
              User
            </option>
            <option value={ROLE_MANAGER}>Manager</option>
            <option value={ROLE_ADMIN}>Admin</option>
          </Select>
        </InputItem>
      </BodyInputContainer>
      <ButtonContainer>
        <Button
          type="submit"
          text={saveButtonName}
          isPrimary={true}
          width="auto"
          onClick={() => {
            if (confirmPasswordRef.current?.value !== passwordRef.current?.value) {
              alert("확인 비밀번호를 정확히 입력하세요.");
              return;
            } else if (nameRef.current?.value === "") {
              alert("이름을 입력하세요.");
              return;
            }
            saveButtonOnClick({
              id: emailRef.current?.value || "",
              password: passwordRef.current?.value || "",
              name: nameRef.current?.value || "",
              role: (roleRef.current?.value as UserRoleType) || ROLE_ANONYMOUS,
            });
          }}
        />
        <Button text="Cancel" isPrimary={false} width="auto" onClick={cancelButtonOnClick} />
      </ButtonContainer>
    </>
  );
};

export default UserDetail;
