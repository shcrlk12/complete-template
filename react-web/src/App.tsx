import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
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

const App = () => {
  const { isLoading } = useSelector((state: RootState) => state.appReducer);

  return (
    <Router>
      <GlobalStyles />
      <Header projectVersion={projectVersion} headerNavList={headerNavList} rightVisible={true} />
      <Loading isLoading={isLoading} />
      <Routes>
        <Route path={Paths.login.path} element={<Login />} />
        <Route path={Paths.availability.annually.path} element={<AvailabilityManagementAnnually />} />
        <Route path={Paths.availability.daily.path} element={<AvailabilityManagementDaily />} />
        <Route path={Paths.users.management.path} element={<UserManagement />}></Route>
        <Route path={Paths.users.new.path} element={<NewUser />}></Route>
        <Route path={Paths.users.modify.path} element={<ModifyUser />}></Route>

        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
};

export default App;
