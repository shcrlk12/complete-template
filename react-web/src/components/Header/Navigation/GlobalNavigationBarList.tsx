import React from "react";
import { HeaderNavList } from "../Header";
import GlobalNavigationBarItem from "./GlobalNavigationBarItem";
import { ProjectVersion } from "../../../Config";
import { StyledGlobalNavigationBarList } from "./NavigationBar.styled";
import { isAuthentication } from "@src/App";
import { useSelector } from "react-redux";
import { RootState } from "@src/index";

type GlobalNavigationBarProps = {
  headerNavList: HeaderNavList[];
  projectVersion: ProjectVersion;
};

const GlobalNavigationBarList = ({ headerNavList, projectVersion }: GlobalNavigationBarProps) => {
  const userRole = useSelector((store: RootState) => store.userReducer.user.role);

  return (
    <StyledGlobalNavigationBarList>
      {headerNavList.map(
        (item) =>
          item.allowVersion <= projectVersion &&
          isAuthentication(userRole, item.userRole) && (
            <GlobalNavigationBarItem key={item.GNBName} headerNavItem={item} projectVersion={projectVersion} />
          ),
      )}
    </StyledGlobalNavigationBarList>
  );
};

export default GlobalNavigationBarList;
