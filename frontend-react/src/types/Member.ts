// 검색 조건
export interface MemberSearch {
  email?: string;
  nickname?: string;
  role?: 'USER' | 'ADMIN';
  tab?: 'active' | 'inactive';
}

// 회원 목록 응답
export interface MemberResult {
  id: number;
  username: string;
  email: string;
  nickname: string;
  role: 'USER' | 'ADMIN';
  status: 'ACTIVE' | 'INACTIVE';
  deleted: boolean;
  createdAt: string;
  updatedAt: string;
}

// 회원 상세 응답
export interface MemberDetail {
  id: number;
  username: string;
  email: string;
  nickname: string;
  role: 'USER' | 'ADMIN';
  status: string; // Enum 대신 문자열 매핑
  deleted: boolean;
  createdAt: string;
  updatedAt: string;
}
