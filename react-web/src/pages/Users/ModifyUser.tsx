import UserDetail from "@components/Users/UserDetail";
import { CommonContainer, CommonInner } from "@pages/Common.styled";
import { resetLoading, setLoading } from "@reducers/appAction";
import { ROLE_ANONYMOUS, ROLE_USER, User, userTypeInitialize } from "@reducers/userActions";
import { Paths } from "@src/Config";
import useInits from "@src/hooks/useInits";
import { fetchData, statusOk } from "@src/util/fetch";
import { useEffect, useState } from "react";
import { useParams } from "react-router";

const ModifyUser = () => {
  const { dispatch, navigate } = useInits();
  const { userId } = useParams();
  const [userDetail, setUserDetail] = useState<User>(userTypeInitialize);

  useEffect(() => {
    fetchData(dispatch, navigate, async () => {
      const response = await fetch("http://182.208.91.171:6789/api/user/" + userId, {
        mode: "cors",
        method: "GET",
        credentials: "include",
      });

      await statusOk(response);
      const data: User = await response.json();

      setUserDetail({
        id: data.id,
        name: data.name,
        role: data.role,
      });
    });
  }, []);

  return (
    <>
      <CommonContainer>
        <CommonInner>
          <UserDetail
            title="유저 수정"
            isIdDisable={true}
            saveButtonName="유저 수정"
            userDetail={userDetail}
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
