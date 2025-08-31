import axiosInstance from '@/api/axiosInstance';
import type { MemberResult, MemberDetail, MemberSearch } from '@/types/Member';

// 회원 목록 조회
export async function fetchMembers(params?: MemberSearch): Promise<MemberResult[]> {
  const res = await axiosInstance.get('/admin/members', { params });
  return res.data;
}

// 회원 상세 조회
export async function fetchMemberDetail(id: number): Promise<MemberDetail> {
  const res = await axiosInstance.get(`/admin/members/${id}`);
  return res.data;
}

// 회원 상태 변경
export async function updateMemberStatus(id: number, status: 'ACTIVE' | 'INACTIVE') {
  await axiosInstance.patch(`/admin/members/${id}/status`, { status });
}

// 회원 탈퇴 여부 변경
export async function updateMemberDeleted(id: number, deleted: boolean) {
  await axiosInstance.patch(`/admin/members/${id}/deleted`, { deleted });
}
