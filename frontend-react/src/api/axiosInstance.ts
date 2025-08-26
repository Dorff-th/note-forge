import axios from 'axios';
import { store } from '@store/index'; // Redux store
//import { logout, refreshToken } from '@store/slices/authSlice';
import { startLoading, stopLoading } from '@store/slices/loadingSlice';
//import { addToast } from '@store/slices/toastSlice';

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const axiosInstance = axios.create({
  baseURL: API_BASE,
  headers: {
    'Content-Type': 'application/json',
    timeout: 5000,
  },
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
  (config) => {
    store.dispatch(startLoading());
    return config;
  },
  (error) => {
    store.dispatch(stopLoading());
    return Promise.reject(error);
  },
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  (response) => {
    store.dispatch(stopLoading());
    return response;
  },
  (error) => {
    store.dispatch(stopLoading());
    // store.dispatch(
    //   addToast({ message: error.response?.data?.message || '에러 발생', type: 'error' }),
    // );
    return Promise.reject(error);
  },
);

export default axiosInstance;
