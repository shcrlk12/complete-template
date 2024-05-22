import React, { useEffect } from "react";
import { CommonContainer, CommonInner, HeaderContainer, UserSearchContainer } from "../Common.styled";
import SearchInput from "@components/Input/SearchInput";
import UserManagementTable from "@components/Table/Pagination/UserManagementTable";
import Button from "@components/Button/Button";
import { useNavigate } from "react-router-dom";
import { Paths } from "@src/Config";
import { useDispatch } from "react-redux";
import { User } from "@reducers/userActions";
import { setLoading } from "@reducers/appAction";

const UserManagement = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  return (
    <>
      <CommonContainer>
        <CommonInner>
          <HeaderContainer>
            <div>Users</div>
            <Button
              isPrimary={false}
              text="새로 만들기"
              width="auto"
              onClick={() => {
                navigate(Paths.users.new.path);
              }}
            />
          </HeaderContainer>
          <UserManagementTable />
        </CommonInner>
      </CommonContainer>
    </>
  );
};

export default UserManagement;
