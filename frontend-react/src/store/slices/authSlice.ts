// src/store/slices/authSlice.ts
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';
import axiosInstance from '@/api/axiosInstance';

interface AuthState {
  token: string | null;
  username: string | null;
  role: string | null; // ✅ role 추가
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  token: localStorage.getItem('accessToken'), // ✅ 로컬스토리지 동기화
  username: localStorage.getItem('username'),
  role: localStorage.getItem('role'),
  isAuthenticated: !!localStorage.getItem('accessToken'),
};

// 로그인
export const login = createAsyncThunk(
  'auth/login',
  async (credentials: { username: string; password: string }) => {
    const res = await axiosInstance.post('/auth/login', credentials);

    console.log('Login response data:', res.data);
    // 기대 응답: { accessToken, refreshToken, user: { username, role } }

    return res.data;
  },
);

// 토큰 재발급
export const refreshToken = createAsyncThunk('auth/refreshToken', async () => {
  const res = await axiosInstance.post('/auth/refresh');
  return res.data; // { accessToken }
});

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginSuccess: (
      state,
      action: PayloadAction<{ token: string; username: string; role: string }>, // ✅ role 포함
    ) => {
      state.token = action.payload.token;
      state.username = action.payload.username;
      state.role = action.payload.role;
      state.isAuthenticated = true;

      // 로컬스토리지에도 저장
      localStorage.setItem('accessToken', action.payload.token);
      localStorage.setItem('username', action.payload.username);
      localStorage.setItem('role', action.payload.role);
    },
    logout: (state) => {
      state.token = null;
      state.username = null;
      state.role = null; // ✅ role 초기화
      state.isAuthenticated = false;

      localStorage.removeItem('accessToken');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
    },
  },
});

export const { logout, loginSuccess } = authSlice.actions;
export default authSlice.reducer;
