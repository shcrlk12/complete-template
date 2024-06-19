//types
export const SET_LOADING = "SET_LOADING" as const;
export const RESET_LOADING = "RESET_LOADING" as const;
export const HEADER_ITEM_VISIBLE = "HEADER_ITEM_VISIBLE" as const;
export const SET_WARRANTY_DATE = "SET_WARRANTY_DATE" as const;

export type HeaderTiemVisible = {
  left: boolean;
  gnb: boolean;
  logoutBtn: boolean;
};

export const setStartOfWarrantyDate = (date: Date) => ({
  type: SET_WARRANTY_DATE,
  payload: date.toISOString(),
});

// action 생성 함수
export const setLoading = () => ({
  type: SET_LOADING,
});

export const resetLoading = () => ({
  type: RESET_LOADING,
});

export const headerTiemVisible = (headerTiemVisible: HeaderTiemVisible) => ({
  type: SET_LOADING,
  payload: headerTiemVisible,
});
