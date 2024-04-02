import { LOGIN_SUCCESS, LOGOUT, User, loginSuccess, logout } from "./userActions";

//Action Type
type UserAction = ReturnType<typeof loginSuccess> | ReturnType<typeof logout>;

//State type
type UserState = {
  isAuthenticated: boolean;
  user: User | null;
};

const initialState: UserState = {
  isAuthenticated: false,
  user: null,
};

const userReducer = (state: UserState = initialState, action: UserAction) => {
  switch (action.type) {
    case LOGIN_SUCCESS:
      return {
        ...state,
        isAuthenticated: true,
        user: action.payload,
      };
    case LOGOUT:
      return {
        ...state,
        isAuthenticated: false,
        user: null,
      };
    default:
      return state;
  }
};

export default userReducer;
