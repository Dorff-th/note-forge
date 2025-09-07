export interface RegisterRequest {
  nickname: string;
  email: string;
  password: string;
  confirmPassword: string;
  emailChecked: boolean;
  nicknameChecked: boolean;
}
