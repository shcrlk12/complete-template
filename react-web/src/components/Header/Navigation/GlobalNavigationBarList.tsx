import React from "react";
import { HeaderNavList } from "../Header";
import GlobalNavigationBarItem from "./GlobalNavigationBarItem";
import { ProjectVersion } from "../../../Config";
import { StyledGlobalNavigationBarList } from "./NavigationBar.styled";

type GlobalNavigationBarProps = {
  headerNavList: HeaderNavList[];
  projectVersion: ProjectVersion;
};

const GlobalNavigationBarList = ({ headerNavList, projectVersion }: GlobalNavigationBarProps) => {
  return (
    <StyledGlobalNavigationBarList>
      {headerNavList.map(
        (item) =>
          item.allowVersion <= projectVersion && (
            <GlobalNavigationBarItem key={item.GNBName} headerNavItem={item} projectVersion={projectVersion} />
          ),
      )}
    </StyledGlobalNavigationBarList>
  );
};

export default GlobalNavigationBarList;
