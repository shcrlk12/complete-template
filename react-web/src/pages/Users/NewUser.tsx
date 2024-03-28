import { CommonContainer, CommonInner } from "@pages/Common.styled";
import UserDetail from "./../../components/Users/UserDetail";
import { useNavigate } from "react-router";

const NewUser = () => {
  const nav = useNavigate();

  return (
    <>
      <CommonContainer>
        <CommonInner>
          <UserDetail
            title="새 유저"
            saveButtonName="유저 추가"
            saveButtonOnClick={() => {}}
            cancelButtonOnClick={() => {
              nav("/users/management");
            }}
          />
        </CommonInner>
      </CommonContainer>
    </>
  );
};

export default NewUser;
