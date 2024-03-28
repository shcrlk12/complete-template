import React from "react";
import ReactDOM from "react-dom/client";
import theme from "@components/style/Theme";
import { ThemeProvider } from "styled-components";
import { combineReducers, configureStore } from "@reduxjs/toolkit";
import { Provider } from "react-redux";
import App from "./App";
import userReducer from "./reducers/userReducer";

const rootReducer = combineReducers({ userReducer });

const store = configureStore({
  reducer: rootReducer,
  devTools: true,
});

const root = ReactDOM.createRoot(document.getElementById("root") as HTMLElement);
root.render(
  <React.StrictMode>
    <Provider store={store}>
      <ThemeProvider theme={theme}>
        <App />
      </ThemeProvider>
    </Provider>
  </React.StrictMode>,
);
