export interface UserProfile {
  profileImageUrl?: string; // null 가능성이 있으니 옵셔널
  username: string;
  nickname: string;
  role: 'USER' | 'ADMIN';
  createdAt: string;
  updatedAt: string;
}
