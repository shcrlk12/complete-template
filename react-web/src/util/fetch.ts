import { resetLoading, setLoading } from "@reducers/appAction";
import { Dispatch, UnknownAction } from "redux";
import { ErrorWithCode } from "./../error/ErrorWithCode";
import { ErrorCode } from "@src/error/ErrorCode";
import { NavigateFunction } from "react-router";
import { logout } from "@reducers/userActions";

export const expireSession = (dispatch: Dispatch<UnknownAction>, navigate: NavigateFunction) => {
  dispatch(logout());
  navigate("/login");
};

export const fetchData = async (
  dispatch: Dispatch<UnknownAction>,
  navigate: NavigateFunction,
  callback?: any,
  errorCallback?: any,
) => {
  dispatch(setLoading());

  try {
    await callback();
  } catch (e: any) {
    if (e.code === ErrorCode.AUTHENTICATION_ENTRY_POINT) {
      expireSession(dispatch, navigate);
    }
    errorCallback(e);
  } finally {
    dispatch(resetLoading());
  }
};

export const statusOk = async (response: Response) => {
  if (!response.ok) {
    const data = await response.json();
    console.log(data);

    throw new ErrorWithCode(data.code, data.message);
  }
};
