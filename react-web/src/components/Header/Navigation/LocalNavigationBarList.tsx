import React from "react";
import { Link } from "react-router-dom";
import { ProjectVersion } from "../../../Config";
import { LocalNavigationBarInnter, StyledLocalNavigationBarList } from "./NavigationBar.styled";
import { UserRoleType } from "@reducers/userActions";
import { useSelector } from "react-redux";
import { RootState } from "@src/index";
import { isAuthentication } from "@src/App";

type LocalNavigationBarProps = {
  display: boolean;
  navList: {
    name: string;
    link: string;
    allowVersion: number;
    userRole: UserRoleType;
  }[];
  onMouseEnter: () => void;
  onMouseLeave: () => void;
  projectVersion: ProjectVersion;
};

const LocalNavigationBarList = ({
  display,
  navList,
  onMouseEnter,
  onMouseLeave,
  projectVersion,
}: LocalNavigationBarProps) => {
  const userRole = useSelector((store: RootState) => store.userReducer.user.role);

  return (
    <StyledLocalNavigationBarList $display={display} onMouseEnter={onMouseEnter} onMouseLeave={onMouseLeave}>
      <LocalNavigationBarInnter>
        {navList.map(
          (item, index) =>
            item.allowVersion <= projectVersion &&
            isAuthentication(userRole, item.userRole) && (
              <li key={index}>
                <Link to={item.link}>{item.name}</Link>
              </li>
            ),
        )}
      </LocalNavigationBarInnter>
    </StyledLocalNavigationBarList>
  );
};

export default LocalNavigationBarList;
