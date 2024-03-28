import React, { useState } from "react";
import { HeaderNavList } from "../Header";
import LocalNavigationBarList from "./LocalNavigationBarList";
import { ProjectVersion } from "../../../Config";
import { StyledGlobalNavigationBarItem, GNBName } from "./NavigationBar.styled";

type GlobalNavigationBarItemProps = {
  headerNavItem: HeaderNavList;
  projectVersion: ProjectVersion;
};

const GlobalNavigationBarItem = ({ headerNavItem, projectVersion }: GlobalNavigationBarItemProps) => {
  const [enterGNB, setEnterGNB] = useState<boolean>(false);
  const [enterLNB, setEnterLNB] = useState<boolean>(false);
  const onGNBMouseEnter = () => {
    setEnterGNB(true);
  };
  const onGNBMouseLeave = () => {
    setEnterGNB(false);
  };
  const onLNBMouseEnter = () => {
    setEnterLNB(true);
  };
  const onLNBMouseLeave = () => {
    setEnterLNB(false);
  };
  return (
    <StyledGlobalNavigationBarItem onMouseEnter={onGNBMouseEnter} onMouseLeave={onGNBMouseLeave}>
      <GNBName>{headerNavItem.GNBName}</GNBName>
      <LocalNavigationBarList
        display={enterGNB || enterLNB}
        onMouseEnter={onLNBMouseEnter}
        onMouseLeave={onLNBMouseLeave}
        navList={headerNavItem.LNBList}
        projectVersion={projectVersion}
      />
    </StyledGlobalNavigationBarItem>
  );
};

export default GlobalNavigationBarItem;
