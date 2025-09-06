import axiosInstance from '@/api/axiosInstance';
import type { UserProfile } from '@/types/User';

export async function getMyProfile(): Promise<UserProfile> {
  const res = await axiosInstance.get<UserProfile>('/members/me');
  return res.data;
}
