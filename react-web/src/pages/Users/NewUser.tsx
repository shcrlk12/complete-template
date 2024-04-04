import { CommonContainer, CommonInner } from "@pages/Common.styled";
import UserDetail from "./../../components/Users/UserDetail";
import { useNavigate } from "react-router";
import { User } from "@reducers/userActions";
import { Paths } from "@src/Config";
import { initPage, routePage } from "@src/App";
import { useDispatch } from "react-redux";
import { useEffect } from "react";

const NewUser = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    initPage(dispatch);
  }, []);

  const addUser = (props: User) => {
    console.log("test1234");

    fetch("http://www.localhost:6789/api/user/new", {
      mode: "cors",
      method: "POST",
      credentials: "include",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ id: props.id, password: props.password, role: props.role, name: props.name }),
    })
      .then((res) => {
        res.json().then((data) => {
          if (data === true) {
            alert("유저 생성 완료.");
            navigate(Paths.users.management.path);
          } else if (data === false) {
            alert("유저 생성 실패");
          }
        });
      })
      .catch((e) => console.warn(e));
  };
  return (
    <>
      <CommonContainer>
        <CommonInner>
          <UserDetail
            title="새 유저"
            saveButtonName="유저 추가"
            saveButtonOnClick={addUser}
            cancelButtonOnClick={() => {
              navigate(Paths.users.management.path);
            }}
          />
        </CommonInner>
      </CommonContainer>
    </>
  );
};

export default NewUser;
