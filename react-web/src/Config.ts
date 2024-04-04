import { HeaderNavList } from "@components/Header/Header";
import { ROLE_ADMIN, ROLE_MANAGER, ROLE_USER } from "@reducers/userActions";

//type settings
export type PathDetail = {
  path: string;
  title: string;
};

export type PathType = {
  login: PathDetail;
  logout: PathDetail;
  availability: {
    annually: PathDetail;
    daily: PathDetail;
    settings: PathDetail;
    logs: PathDetail;
  };
  reports: {
    static: PathDetail;
    memo: PathDetail;
  };
  users: {
    management: PathDetail;
    new: PathDetail;
    modify: PathDetail;
  };
};

export const TYPE1_VERSION = 0.1 as const;
export const TYPE2_VERSION = 0.2 as const;
export const TYPE3_VERSION = 0.3 as const;

export type ProjectVersion = typeof TYPE1_VERSION | typeof TYPE2_VERSION | typeof TYPE3_VERSION;

//config settings
export const Paths: PathType = {
  login: { path: "/login", title: "로그인" },
  logout: { path: "/logout", title: "로그아웃" },
  availability: {
    annually: { path: "/availability/annually", title: "연간 가동률 현황" },
    daily: { path: "/availability/daily", title: "일간 가동률 현황" },
    settings: { path: "/availability/settings", title: "Settings" },
    logs: { path: "/availability/logs", title: "Logs" },
  },
  reports: {
    static: { path: "/reports/static", title: "Static Report" },
    memo: { path: "/reports/memo", title: "Memo Report" },
  },
  users: {
    management: { path: "/users/management", title: "User Management" },
    new: { path: "/users/new", title: "새 유저" },
    modify: { path: "/users/modify", title: "유저 수정" },
  },
};

export const projectVersion: ProjectVersion = TYPE1_VERSION;

export const headerNavList: HeaderNavList[] = [
  {
    GNBName: "Availability",
    allowVersion: TYPE1_VERSION,
    userRole: ROLE_USER,
    LNBList: [
      {
        name: "연간 가동률 현황",
        link: Paths.availability.annually.path,
        allowVersion: TYPE1_VERSION,
        userRole: ROLE_USER,
      },
      {
        name: "일간 가동률 현황",
        link: Paths.availability.daily.path,
        allowVersion: TYPE1_VERSION,
        userRole: ROLE_MANAGER,
      },
      {
        name: "Settings",
        link: Paths.availability.settings.path,
        allowVersion: TYPE3_VERSION,
        userRole: ROLE_MANAGER,
      },
      {
        name: "Logs",
        link: Paths.availability.logs.path,
        allowVersion: TYPE2_VERSION,
        userRole: ROLE_MANAGER,
      },
    ],
  },
  {
    GNBName: "Reports",
    allowVersion: TYPE2_VERSION,
    userRole: ROLE_USER,
    LNBList: [
      {
        name: "Static report",
        link: Paths.reports.static.path,
        allowVersion: TYPE2_VERSION,
        userRole: ROLE_USER,
      },
      {
        name: "Memo report",
        link: Paths.reports.memo.path,
        allowVersion: TYPE2_VERSION,
        userRole: ROLE_USER,
      },
    ],
  },
  {
    GNBName: "Users",
    allowVersion: TYPE1_VERSION,
    userRole: ROLE_ADMIN,
    LNBList: [
      {
        name: "User Management",
        link: Paths.users.management.path,
        allowVersion: TYPE1_VERSION,
        userRole: ROLE_ADMIN,
      },
    ],
  },
];
