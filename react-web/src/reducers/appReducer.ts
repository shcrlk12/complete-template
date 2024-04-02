import { RESET_LOADING, SET_LOADING, resetLoading, setLoading } from "./appAction";

//Action Type
type AppAction = ReturnType<typeof setLoading> | ReturnType<typeof resetLoading>;

//State type
type AppState = {
  isLoading: boolean;
};

const initialState: AppState = {
  isLoading: false,
};

const appReducer = (state: AppState = initialState, action: AppAction) => {
  switch (action.type) {
    case SET_LOADING:
      return {
        ...state,
        isLoading: true,
      };
    case RESET_LOADING:
      return {
        ...state,
        isLoading: false,
      };
    default:
      return state;
  }
};

export default appReducer;
