import React from "react";
import { CommonContainer, CommonInner, HeaderContainer, UserSearchContainer } from "../Common.styled";
import SearchInput from "@components/Input/SearchInput";
import UserManagementTable from "@components/Table/Pagination/UserManagementTable";
import Button from "@components/Button/Button";
import { useNavigate } from "react-router-dom";

const UserManagement = () => {
  const nav = useNavigate();
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
                nav("/users/new");
              }}
            />
          </HeaderContainer>
          <UserSearchContainer>
            <SearchInput height={40} text="Search users" />
          </UserSearchContainer>
          <UserManagementTable />
        </CommonInner>
      </CommonContainer>
    </>
  );
};

export default UserManagement;
