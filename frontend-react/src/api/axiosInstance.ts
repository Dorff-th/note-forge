import axios from 'axios';
import { store } from '@store/index'; // Redux store
import { logout } from '@store/slices/authSlice';
import { startLoading, stopLoading } from '@store/slices/loadingSlice';
import { showToast } from '@store/slices/toastSlice';

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

    const state = store.getState();
    const token = state.auth.token || localStorage.getItem('accessToken');

    if (token) {
      config.headers?.set('Authorization', `Bearer ${token}`);
    }

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

    const { dispatch } = store;
    if (!error.response) {
      // 네트워크 단절, CORS 등
      dispatch(showToast({ type: 'error', message: '네트워크 오류 발생' }));
      return Promise.reject(error);
    }

    const { status } = error.response;

    switch (status) {
      case 400:
        dispatch(showToast({ type: 'error', message: '잘못된 요청입니다.' }));
        break;
      case 401:
        dispatch(showToast({ type: 'error', message: '로그인이 필요합니다.' }));
        dispatch(logout()); // or refresh token 시도
        break;
      case 403:
        dispatch(showToast({ type: 'warning', message: '접근 권한이 없습니다.' }));
        break;
      case 404:
        dispatch(showToast({ type: 'error', message: '요청한 데이터를 찾을 수 없습니다.' }));
        break;
      case 409:
        dispatch(showToast({ type: 'error', message: '이미 존재하는 데이터입니다.' }));
        break;
      case 500:
        dispatch(showToast({ type: 'error', message: '서버 오류가 발생했습니다.' }));
        break;
      default:
        dispatch(showToast({ type: 'error', message: `알 수 없는 오류: ${status}` }));
    }

    return Promise.reject(error);
  },
);

export default axiosInstance;
