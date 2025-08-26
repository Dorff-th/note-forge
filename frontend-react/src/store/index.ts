import { configureStore } from '@reduxjs/toolkit';
import authReducer from '@store/slices/authSlice';
import loadingReducer from '@store/slices/loadingSlice';
import toastReducer from '@store/slices/toastSlice';

export const store = configureStore({
  reducer: {
    auth: authReducer,
    loading: loadingReducer,
    toast: toastReducer,
  },
});

// 타입 추론용
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
