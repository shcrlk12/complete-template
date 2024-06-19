import { resetLoading, setLoading } from "@reducers/appAction";
import { ErrorCode } from "@src/error/ErrorCode";
import { expireSession } from "@src/util/fetch";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";

const useDownloadFile = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const downloadFile = async (url: string, option: RequestInit, errorCallback?: (error: any) => void) => {
    dispatch(setLoading());

    try {
      const response = await fetch(url, option);

      const blob = await response.blob();
      const blobUrl = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(blobUrl);
    } catch (e: any) {
      if (e.code === ErrorCode.AUTHENTICATION_ENTRY_POINT) {
        expireSession(dispatch, navigate);
      }
      if (errorCallback) {
        errorCallback(e);
      } else {
        console.error("Error fetching data:", e);
      }
      throw e;
    } finally {
      dispatch(resetLoading());
    }
  };
  return downloadFile;
};

export default useDownloadFile;
