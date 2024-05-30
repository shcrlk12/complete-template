import { CommonContainer, CommonInner } from "@pages/Common.styled";
import UserDetail from "./../../components/Users/UserDetail";
import { User } from "@reducers/userActions";
import { Paths } from "@src/Config";
import { useEffect } from "react";
import useInits from "@src/hooks/useInits";
import { fetchData, statusOk } from "@src/util/fetch";

const NewUser = () => {
  const { dispatch, navigate } = useInits();

  useEffect(() => {}, []);

  const addUserOnClick = (props: User) => {
    fetchData(dispatch, navigate, async () => {
      const response = await fetch("http://182.208.91.171:6789/api/user/new", {
        mode: "cors",
        method: "POST",
        credentials: "include",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ id: props.id, password: props.password, role: props.role, name: props.name }),
      });

      await statusOk(response);
      const data = await response.json();

      if (data === true) {
        alert("유저 생성 완료.");
        navigate(Paths.users.management.path);
      } else if (data === false) {
        alert("유저 생성 실패");
      }
    });
  };

  return (
    <>
      <CommonContainer>
        <CommonInner>
          <UserDetail
            title="새 유저"
            saveButtonName="유저 추가"
            saveButtonOnClick={addUserOnClick}
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
