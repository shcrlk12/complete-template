import React, { useCallback, useState } from "react";
import logo from "@images/logo/UnisonLogo140px.png";
import Button from "../Button/Button";
import { useLocation } from "react-router";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { logout } from "../../reducers/userActions";
import GlobalNavigationBar from "./Navigation/GlobalNavigationBarList";
import { PathDetail, PathType, Paths, ProjectVersion } from "../../Config";
import { HeaderInner, LeftHeaderContainer, Logo, PageTitle, RightHeaderContainer, StyledHeader } from "./Header.styled";

export type HeaderNavList = {
  GNBName: string;
  allowVersion: number;
  LNBList: {
    name: string;
    link: string;
    allowVersion: number;
  }[];
};

type HeaderProps = {
  headerNavList: HeaderNavList[];
  projectVersion: ProjectVersion;
  rightVisible?: boolean;
};

const Header = ({ headerNavList, projectVersion, rightVisible = true }: HeaderProps) => {
  const [pageTitle, setPageTitle] = useState("");

  const dispatch = useDispatch();
  const location = useLocation();
  const navigate = useNavigate();

  /**
   * Returns a title of current page, This is parsed on the current path.
   *
   * @param configuredPaths a contents of config file, Config.ts
   * @param changingPath it can set path
   */
  const getCurrentPathTitle = useCallback(
    (configuredPaths: PathType | PathDetail | any, changingPath: string): string => {
      for (const key in configuredPaths) {
        if (typeof configuredPaths[key] === "object") {
          if (configuredPaths[key].path?.includes(changingPath)) {
            return configuredPaths[key].title;
          } else {
            let title = getCurrentPathTitle(configuredPaths[key], changingPath);

            if (title !== "undefined") {
              return title;
            }
          }
        }
      }
      return "undefined";
    },
    [],
  );

  useEffect(() => {
    setPageTitle(getCurrentPathTitle(Paths, location.pathname));
  }, [getCurrentPathTitle, location.pathname]);

  return (
    <StyledHeader>
      <HeaderInner>
        <LeftHeaderContainer>
          <Logo
            src={logo}
            alt="unison logo"
            onClick={() => {
              navigate(Paths.availability.annually.path);
            }}
          />
          <PageTitle>{pageTitle}</PageTitle>
        </LeftHeaderContainer>
        {rightVisible && (
          <RightHeaderContainer>
            <GlobalNavigationBar headerNavList={headerNavList} projectVersion={projectVersion} />
            <Button
              isPrimary={true}
              text="Logout"
              onClick={() => {
                dispatch(logout());
                navigate(Paths.logout.path);
              }}
            />
          </RightHeaderContainer>
        )}
      </HeaderInner>
    </StyledHeader>
  );
};

export default Header;
