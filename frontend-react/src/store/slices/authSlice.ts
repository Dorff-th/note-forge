// src/store/slices/authSlice.ts
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';
import axiosInstance from '@/api/axiosInstance';

interface User {
  id: number;
  username: string;
  role: string;
  nickname: string;
  profileImageUrl?: string | null;
}

interface AuthState {
  token: string | null;
  user: User | null;
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  token: localStorage.getItem('accessToken'),
  user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')!) : null,
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
    loginSuccess: (state, action: PayloadAction<{ token: string; user: User }>) => {
      state.token = action.payload.token;
      state.user = action.payload.user;
      state.isAuthenticated = true;

      // 로컬스토리지 동기화
      localStorage.setItem('accessToken', action.payload.token);
      localStorage.setItem('user', JSON.stringify(action.payload.user));
    },
    logout: (state) => {
      state.token = null;
      state.user = null;
      state.isAuthenticated = false;

      localStorage.removeItem('accessToken');
      localStorage.removeItem('user');
    },
    // ✅ 프로필 이미지 변경 (닉네임 변경 등도 재활용 가능)
    updateUser: (state, action: PayloadAction<Partial<User>>) => {
      if (state.user) {
        state.user = { ...state.user, ...action.payload };
        localStorage.setItem('user', JSON.stringify(state.user));
      }
    },
  },
});

export const { logout, loginSuccess, updateUser } = authSlice.actions;
export default authSlice.reducer;
