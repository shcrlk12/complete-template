import UserDetail from "@components/Users/UserDetail";
import { CommonContainer, CommonInner } from "@pages/Common.styled";
import { initPage, routePage } from "@src/App";
import { Paths } from "@src/Config";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";

const ModifyUser = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    initPage(dispatch);
  }, []);

  return (
    <>
      <CommonContainer>
        <CommonInner>
          <UserDetail
            title="유저 수정"
            saveButtonName="유저 수정"
            saveButtonOnClick={() => {}}
            cancelButtonOnClick={() => {
              navigate(Paths.users.management.path);
            }}
          />
        </CommonInner>
      </CommonContainer>
    </>
  );
};

export default ModifyUser;
