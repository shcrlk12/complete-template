import React, { FormEvent, useEffect } from "react";
import Input from "../components/Input/Input";
import styled from "styled-components";
import RadioButton from "../components/CheckBox";
import Button from "../components/Button/Button";
import { useDispatch } from "react-redux";
import { loginSuccess, logout } from "../reducers/userActions";
import { useNavigate } from "react-router";
import { Paths } from "../Config";
import { resetLoading, setLoading } from "@reducers/appAction";

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

  useEffect(() => {
    dispatch(resetLoading());
    dispatch(logout());
  }, []);

  const loginSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    let formData = new FormData(event.currentTarget);

    fetch("http://www.localhost:6789/login", {
      mode: "cors",
      method: "POST",
      credentials: "include",
      body: formData,
    })
      .then((res) => {
        res.json().then((data) => {
          if (data.status === 200) {
            dispatch(loginSuccess({ id: data.id, name: "jeongwon", role: data.role }));
            dispatch(setLoading());
            nav(Paths.availability.annually.path);
          } else {
            alert(data.message);
          }
        });
      })
      .catch((e) => console.warn(e));
  };

  return (
    <>
      <Section>
        <LoginContainer>
          <form onSubmit={loginSubmit}>
            <LoginHeader>Login</LoginHeader>
            <InputContainer>
              <InputLabel>Email</InputLabel>
              <Input type="email" name="username" placeholder="email" />
            </InputContainer>
            <InputContainer>
              <InputLabel>password</InputLabel>
              <Input type="password" name="password " placeholder="password" />
            </InputContainer>
            <RadioButton />
            <LoginButtonContainer>
              <Button
                type="submit"
                isPrimary={true}
                text="Log in"
                width="100%"
                // onClick={() => {
                //   dispatch(loginSuccess({ id: "kjwon", username: "jeongwon", role: ROLE_USER }));
                //   nav(Paths.availability.annually.path);
                // }}
              />
            </LoginButtonContainer>
          </form>
        </LoginContainer>
      </Section>
    </>
  );
};

export default Login;
