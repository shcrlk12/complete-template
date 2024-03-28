import React from "react";
import Input from "../components/Input/Input";
import styled from "styled-components";
import RadioButton from "../components/CheckBox";
import Button from "../components/Button/Button";
import { useDispatch } from "react-redux";
import { loginSuccess } from "../reducers/userActions";
import { useNavigate } from "react-router";
import { Paths } from "../Config";
import { GENERAL_ROLE } from "./../reducers/userActions";

const Section = styled.div`
  display: flex;
  justify-content: center;
  padding-top: 40px;
`;

const LoginContainer = styled.div`
  width: 400px;
  height: 100%;
`;

const LoginHeader = styled.div`
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  padding-bottom: 8px;
`;

const InputContainer = styled.div`
  height: 84px;
`;

const InputLabel = styled.div`
  height: 32px;
  font-size: 16px;
  font-weight: bold;
`;

const LoginButtonContainer = styled.div`
  padding-top: 15px;
`;

const Login = () => {
  const dispatch = useDispatch();
  const nav = useNavigate();

  return (
    <>
      <Section>
        <LoginContainer>
          <LoginHeader>Login</LoginHeader>
          <InputContainer>
            <InputLabel>Email</InputLabel>
            <Input type="email" placeholder="email" />
          </InputContainer>
          <InputContainer>
            <InputLabel>password</InputLabel>
            <Input type="password" placeholder="password" />
          </InputContainer>
          <RadioButton />
          <LoginButtonContainer>
            <Button
              isPrimary={true}
              text="Log in"
              width="100%"
              onClick={() => {
                dispatch(loginSuccess({ id: "kjwon", username: "jeongwon", role: GENERAL_ROLE }));
                nav(Paths.availability.annually.path);
              }}
            />
          </LoginButtonContainer>
        </LoginContainer>
      </Section>
    </>
  );
};

export default Login;
