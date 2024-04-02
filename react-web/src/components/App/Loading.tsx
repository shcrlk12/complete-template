import React from "react";
import { LoadingInner, Loadingbg } from "./App.styled";
import { CircularProgress } from "@mui/material";

type LoadingProps = {
  isLoading: boolean;
};

const Loading = ({ isLoading }: LoadingProps) => {
  return (
    isLoading && (
      <Loadingbg>
        <LoadingInner>
          <CircularProgress className="loading-icon" />
        </LoadingInner>
      </Loadingbg>
    )
  );
};

export default Loading;
