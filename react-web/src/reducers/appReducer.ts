import {
  RESET_LOADING,
  SET_LOADING,
  resetLoading,
  setLoading,
  headerTiemVisible,
  HeaderTiemVisible,
  setStartOfWarrantyDate,
  SET_WARRANTY_DATE,
} from "./appAction";

//Action Type
type AppAction =
  | ReturnType<typeof setLoading>
  | ReturnType<typeof resetLoading>
  | ReturnType<typeof headerTiemVisible>
  | ReturnType<typeof setStartOfWarrantyDate>;

//State type
type AppState = {
  headerTiemVisible: HeaderTiemVisible;
  isLoading: boolean;
  startOfWarrantyDate: string;
};

const initialState: AppState = {
  headerTiemVisible: {
    left: true,
    gnb: true,
    logoutBtn: true,
  },
  isLoading: false,
  startOfWarrantyDate: new Date().toISOString(),
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
    case SET_WARRANTY_DATE:
      return {
        ...state,
        startOfWarrantyDate: action.payload,
      };
    default:
      return state;
  }
};

export default appReducer;
