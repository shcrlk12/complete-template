import {
  RESET_LOADING,
  SET_LOADING,
  resetLoading,
  setLoading,
  headerTiemVisible,
  HeaderTiemVisible,
} from "./appAction";

//Action Type
type AppAction = ReturnType<typeof setLoading> | ReturnType<typeof resetLoading> | ReturnType<typeof headerTiemVisible>;

//State type
type AppState = {
  headerTiemVisible: HeaderTiemVisible;
  isLoading: boolean;
};

const initialState: AppState = {
  headerTiemVisible: {
    left: true,
    gnb: true,
    logoutBtn: true,
  },
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
