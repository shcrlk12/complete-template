import React from "react";
import { Link } from "react-router-dom";
import { ProjectVersion } from "../../../Config";
import { LocalNavigationBarInnter, StyledLocalNavigationBarList } from "./NavigationBar.styled";

type LocalNavigationBarProps = {
  display: boolean;
  navList: {
    name: string;
    link: string;
    allowVersion: number;
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
  return (
    <StyledLocalNavigationBarList $display={display} onMouseEnter={onMouseEnter} onMouseLeave={onMouseLeave}>
      <LocalNavigationBarInnter>
        {navList.map(
          (item, index) =>
            item.allowVersion <= projectVersion && (
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
