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
import { ROLE_ADMIN, ROLE_ANONYMOUS, ROLE_MANAGER, ROLE_USER, UserRoleType } from "@reducers/userActions";

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
    { path: Paths.availability.annually.path, component: <AvailabilityManagementAnnually /> },
  ];

  const managerRoleRoutes: PageRole[] = [
    ...userRoleRoutes,
    { path: Paths.availability.daily.path, component: <AvailabilityManagementDaily /> },
  ];

  const adminRoleRoutes: PageRole[] = [
    ...managerRoleRoutes,
    { path: Paths.users.management.path, component: <UserManagement /> },
    { path: Paths.users.new.path, component: <NewUser /> },
    { path: Paths.users.modify.path, component: <ModifyUser /> },
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
  const { isLoading } = useSelector((store: RootState) => store.appReducer);
  const userRole = useSelector((store: RootState) => store.userReducer.user.role);

  return (
    <Router>
      <GlobalStyles />
      <Header projectVersion={projectVersion} headerNavList={headerNavList} />
      <Loading isLoading={isLoading} />
      <Routes>
        {getRouteByRole(userRole)}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
};

export default App;
