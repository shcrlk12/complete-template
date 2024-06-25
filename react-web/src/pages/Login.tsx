import React, { FormEvent, useEffect } from "react";
import Input from "../components/Input/Input";
import styled from "styled-components";
import RadioButton from "../components/CheckBox";
import Button from "../components/Button/Button";
import { useDispatch } from "react-redux";
import { loginSuccess } from "../reducers/userActions";
import { useNavigate } from "react-router";
import { Paths, backendServerIp } from "../Config";
import { resetLoading, setLoading } from "@reducers/appAction";
import { fetchData, statusOk } from "@src/util/fetch";
import { parseyymmdd, parseyyyymmdd } from "@src/util/date";

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
  const navigate = useNavigate();

  useEffect(() => {}, []);

  const loginSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    dispatch(setLoading());

    let formData = new FormData(event.currentTarget);

    fetchData(dispatch, navigate, async () => {
      const response = await fetch(`http://${backendServerIp}/api/login`, {
        mode: "cors",
        method: "POST",
        credentials: "include",
        body: formData,
      });

      await statusOk(response);
      const data = await response.json();

      if (data.status === 200) {
        let now = new Date(Date.now());
        let { year } = parseyyyymmdd(now);
        console.log(data);

        dispatch(loginSuccess({ id: data.id, name: "", role: data.role }));
        navigate(`${Paths.availability.annually.path}/${year}`);
      } else {
        alert(data.message);
      }
    });
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
            <LoginButtonContainer>
              <Button type="submit" isPrimary={true} text="Log in" width="100%" />
            </LoginButtonContainer>
          </form>
        </LoginContainer>
      </Section>
    </>
  );
};

export default Login;
