//types
export const LOGIN_SUCCESS = "LOGIN_SUCCESS" as const;
export const LOGOUT = "LOGOUT" as const;
export const GENERAL_ROLE = "GENERAL_ROLE" as const;
export const MANAGER_ROLE = "MANAGER_ROLE" as const;
export const ADMIN_ROLE = "ADMIN_ROLE" as const;

export type User = {
  id: string;
  username: string;
  role: typeof GENERAL_ROLE | typeof MANAGER_ROLE | typeof ADMIN_ROLE;
};

// action 생성 함수
export const loginSuccess = (user: User) => ({
  type: LOGIN_SUCCESS,
  payload: user,
});

export const logout = () => ({
  type: LOGOUT,
});
