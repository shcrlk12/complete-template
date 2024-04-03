import React from "react";
import ReactDOM from "react-dom/client";
import theme from "@components/style/Theme";
import { ThemeProvider } from "styled-components";
import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { Provider } from "react-redux";
import App from "./App";
import userReducer from "./reducers/userReducer";
import appReducer from "./reducers/appReducer";

const rootReducer = combineReducers({ appReducer, userReducer });

const store = configureStore({
  reducer: rootReducer,
  devTools: true,
});

export type RootState = ReturnType<typeof rootReducer>;

const root = ReactDOM.createRoot(document.getElementById("root") as HTMLElement);

const test = () => {
  console.log("Load First");
};
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <ThemeProvider theme={theme}>
        {test()}
        <App />
      </ThemeProvider>
    </Provider>
  </React.StrictMode>,
);
