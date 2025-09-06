import axiosInstance from '@/api/axiosInstance';
import type { UserProfile } from '@/types/User';

// 내 프로필 조회
export async function getMyProfile(): Promise<UserProfile> {
  const res = await axiosInstance.get<UserProfile>('/members/me');
  return res.data;
}

// 프로필 이미지 업로드
export async function uploadProfileImage(file: File): Promise<{ profileImageUrl: string }> {
  const formData = new FormData();
  formData.append('file', file);

  const res = await axiosInstance.patch('/members/me/profile-image', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
  return res.data;
}

// 프로필 이미지 삭제
export async function deleteProfileImage(): Promise<{ profileImageUrl: string | null }> {
  const res = await axiosInstance.delete('/members/me/profile-image');
  return res.data;
}
