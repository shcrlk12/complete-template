import UserDetail from "@components/Users/UserDetail";
import { CommonContainer, CommonInner } from "@pages/Common.styled";
import { useNavigate } from "react-router";

const ModifyUser = () => {
  const nav = useNavigate();

  return (
    <>
      <CommonContainer>
        <CommonInner>
          <UserDetail
            title="유저 수정"
            saveButtonName="유저 수정"
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

export default ModifyUser;
