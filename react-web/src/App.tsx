import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate, NavigateFunction } from "react-router-dom";
import Login from "@pages/Login";
import AvailabilityManagementAnnually from "@pages/Availability/AvailabilityManagementAnnually";
import AvailabilityManagementDaily from "@pages/Availability/AvailabilityManagementDaily";
import GlobalStyles from "@components/style/GlobalStyles";
import UserManagement from "@pages/Users/UserManagement";
import NewUser from "@pages/Users/NewUser";
import ModifyUser from "@pages/Users/ModifyUser";
import { Paths, headerNavList, projectVersion } from "./Config";
import Header from "@components/Header/Header";
import Loading from "@components/App/Loading";
import { useSelector } from "react-redux";
import { RootState } from "src";
import { ROLE_ADMIN, ROLE_ANONYMOUS, ROLE_MANAGER, ROLE_USER, UserRoleType, loginSuccess } from "@reducers/userActions";
import { useEffect } from "react";
import { useDispatch } from "react-redux";
import { Dispatch, UnknownAction } from "redux";
import { resetLoading, setLoading } from "@reducers/appAction";
import { fetchData, statusOk } from "./util/fetch";
import { parseyyyymmdd } from "./util/date";

type PageRole = {
  path: string;
  component: any;
};

export const isAuthentication = (userRole: UserRoleType, thisRole: UserRoleType) => {
  if (thisRole === ROLE_ANONYMOUS) {
    if (userRole === ROLE_ANONYMOUS || userRole === ROLE_USER || userRole === ROLE_MANAGER || userRole === ROLE_ADMIN) {
      return true;
    }
    return false;
  } else if (thisRole === ROLE_USER) {
    if (userRole === ROLE_USER || userRole === ROLE_MANAGER || userRole === ROLE_ADMIN) {
      return true;
    }
    return false;
  } else if (thisRole === ROLE_MANAGER) {
    if (userRole === ROLE_MANAGER || userRole === ROLE_ADMIN) {
      return true;
    }
    return false;
  } else if (thisRole === ROLE_ADMIN) {
    if (userRole === ROLE_ADMIN) {
      return true;
    }
    return false;
  }
};

const getRouteByRole = (userRole: UserRoleType) => {
  const anonymousRoleRoutes: PageRole[] = [{ path: Paths.login.path, component: <Login /> }];

  const userRoleRoutes: PageRole[] = [
    ...anonymousRoleRoutes,
    { path: `${Paths.availability.annually.path}/:year/:month/:day`, component: <AvailabilityManagementAnnually /> },
  ];

  const managerRoleRoutes: PageRole[] = [
    ...userRoleRoutes,
    { path: `${Paths.availability.daily.path}/:year/:month/:day`, component: <AvailabilityManagementDaily /> },
  ];

  const adminRoleRoutes: PageRole[] = [
    ...managerRoleRoutes,
    { path: Paths.users.management.path, component: <UserManagement /> },
    { path: Paths.users.new.path, component: <NewUser /> },
    { path: `${Paths.users.modify.path}/:userId`, component: <ModifyUser /> },
  ];

  let roleRoutes: PageRole[];

  switch (userRole) {
    case ROLE_ADMIN:
      roleRoutes = adminRoleRoutes;
      break;
    case ROLE_MANAGER:
      roleRoutes = managerRoleRoutes;
      break;
    case ROLE_USER:
      roleRoutes = userRoleRoutes;
      break;
    case ROLE_ANONYMOUS:
      roleRoutes = anonymousRoleRoutes;
      break;
    default:
      roleRoutes = [{ path: "", component: "" }];
  }

  return roleRoutes.map((route) => <Route key={route.path} path={route.path} element={route.component} />);
};

const App = () => {
  const userRole = useSelector((store: RootState) => store.userReducer.user.role);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  console.log("App Rendering");

  useEffect(() => {
    fetchData(dispatch, navigate, async () => {
      const response = await fetch("http://www.localhost:6789/api/login/auth", {
        mode: "cors",
        method: "GET",
        credentials: "include",
      });

      await statusOk(response);
      const data = await response.json();
      if (data.role === ROLE_USER || data.role === ROLE_MANAGER || data.role === ROLE_ADMIN) {
        dispatch(loginSuccess({ id: data.id, name: data.name, role: data.role }));

        let now = new Date(Date.now());
        let { year, month, day } = parseyyyymmdd(now);
        navigate(`${Paths.availability.annually.path}/${year}/${month}/${day}`);
      }
    });
  }, []);

  return (
    <>
      <GlobalStyles />
      <Header projectVersion={projectVersion} headerNavList={headerNavList} />
      <Loading />
      <Routes>
        {getRouteByRole(userRole)}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </>
  );
};

export default App;
