//types
export const LOGIN_SUCCESS = "LOGIN_SUCCESS" as const;
export const LOGOUT = "LOGOUT" as const;
export const ROLE_ANONYMOUS = "ROLE_ANONYMOUS" as const;
export const ROLE_USER = "ROLE_USER" as const;
export const ROLE_MANAGER = "ROLE_MANAGER" as const;
export const ROLE_ADMIN = "ROLE_ADMIN" as const;

export type UserRoleType = typeof ROLE_ANONYMOUS | typeof ROLE_USER | typeof ROLE_MANAGER | typeof ROLE_ADMIN;
export type User = {
  id: string;
  password?: string;
  name: string;
  role: UserRoleType;
  lastLoginTime?: string | null;
};

export const userTypeInitialize: User = { id: "", name: "", role: ROLE_USER };

// action 생성 함수
export const loginSuccess = (user: User) => ({
  type: LOGIN_SUCCESS,
  payload: user,
});

export const logout = () => ({
  type: LOGOUT,
});
