import { resetLoading, setLoading } from "@reducers/appAction";
import { ErrorCode } from "@src/error/ErrorCode";
import { expireSession, statusOk } from "@src/util/fetch";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

const useFetchData = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const fetchData = async <T>(url: string, option?: RequestInit, errorCallback?: (error: any) => void): Promise<T> => {
    dispatch(setLoading());

    try {
      const response = await fetch(url, option);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const json = await response.json();
      return json.data as T;
    } catch (e: any) {
      if (e.code === ErrorCode.AUTHENTICATION_ENTRY_POINT) {
        expireSession(dispatch, navigate);
      }
      if (errorCallback) {
        errorCallback(e);
      } else {
        console.error("Error fetching data:", e);
      }
      throw e; // 에러가 발생했음을 호출한 쪽에서 인지할 수 있게 던짐
    } finally {
      dispatch(resetLoading());
    }
  };

  return fetchData;
};

export default useFetchData;
