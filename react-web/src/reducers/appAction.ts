//types
export const SET_LOADING = "SET_LOADING" as const;
export const RESET_LOADING = "RESET_LOADING" as const;

// action 생성 함수
export const setLoading = () => ({
  type: SET_LOADING,
});

export const resetLoading = () => ({
  type: RESET_LOADING,
});
