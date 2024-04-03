import React, { useCallback, useState } from "react";
import logo from "@images/logo/UnisonLogo140px.png";
import Button from "../Button/Button";
import { useLocation } from "react-router";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { ROLE_ANONYMOUS, UserRoleType, logout } from "../../reducers/userActions";
import GlobalNavigationBar from "./Navigation/GlobalNavigationBarList";
import { PathDetail, PathType, Paths, ProjectVersion } from "../../Config";
import { HeaderInner, LeftHeaderContainer, Logo, PageTitle, RightHeaderContainer, StyledHeader } from "./Header.styled";
import { RootState } from "src";
import { useSelector } from "react-redux";

export type HeaderNavList = {
  GNBName: string;
  allowVersion: number;
  userRole: UserRoleType;
  LNBList: {
    name: string;
    link: string;
    allowVersion: number;
    userRole: UserRoleType;
  }[];
};

type HeaderProps = {
  headerNavList: HeaderNavList[];
  projectVersion: ProjectVersion;
};

const Header = ({ headerNavList, projectVersion }: HeaderProps) => {
  const [pageTitle, setPageTitle] = useState("");

  const dispatch = useDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const user = useSelector((store: RootState) => store.userReducer);

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
        {user.isAuthenticated && (
          <RightHeaderContainer>
            <GlobalNavigationBar headerNavList={headerNavList} projectVersion={projectVersion} />
            <Button
              isPrimary={true}
              text="Logout"
              onClick={() => {
                dispatch(logout());
                navigate(Paths.login.path);
                fetch("http://www.localhost:6789/logout", {
                  mode: "cors",
                  method: "POST",
                  credentials: "include",
                })
                  .then((res) => {
                    console.log(res);
                  })
                  .catch((e) => console.warn(e));
              }}
            />
          </RightHeaderContainer>
        )}
      </HeaderInner>
    </StyledHeader>
  );
};

export default Header;
