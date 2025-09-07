import axiosInstance from './axiosInstance';
import type { RegisterRequest } from '@/types/RegisterRequest';

export const register = (data: RegisterRequest) => axiosInstance.post('/auth/register', data);

// 이메일 중복 체크
export const checkEmail = (email: string) =>
  axiosInstance.get(`/auth/register/checked-email`, { params: { email } });

// 닉네임 중복 체크
export const checkNickname = (nickname: string) =>
  axiosInstance.get(`/auth/register/checked-nickname`, { params: { nickname } });
