import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { checkEmail, checkNickname, register } from '@/api/registerApi';
import { withToast } from '@/utils/withToast';
import type { RegisterRequest } from '@/types/RegisterRequest';

export default function UserRegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState<RegisterRequest>({
    nickname: '',
    email: '',
    password: '',
    confirmPassword: '',
    emailChecked: false,
    nicknameChecked: false,
  });

  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErrors({ ...errors, [e.target.name]: '' }); // 입력 시 에러 제거
  };

  const validate = () => {
    const newErrors: { [key: string]: string } = {};

    if (!form.email) newErrors.email = '이메일을 입력하세요.';
    if (!form.nickname) newErrors.nickname = '닉네임을 입력하세요.';
    if (form.nickname && form.nickname.length > 4)
      newErrors.nickname = '닉네임은 최대 4글자까지 가능합니다.';
    if (!form.password) newErrors.password = '비밀번호를 입력하세요.';
    if (form.password && form.password.length < 4)
      newErrors.password = '비밀번호는 최소 4자 이상이어야 합니다.';
    if (form.confirmPassword !== form.password)
      newErrors.confirmPassword = '비밀번호가 일치하지 않습니다.';

    // ✅ 추가: 중복 체크 여부 확인
    if (!form.emailChecked) newErrors.emailChecked = '이메일 중복 체크는 필수입니다.';
    if (!form.nicknameChecked) newErrors.nicknameChecked = '닉네임 중복 체크는 필수입니다.';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleCheckEmail = async () => {
    if (!form.email) {
      setErrors((prev) => ({ ...prev, email: '이메일을 입력하세요.' }));
      return;
    }

    // ✅ 이메일 형식 검증
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email)) {
      setErrors((prev) => ({ ...prev, email: '올바른 이메일 형식이 아닙니다.' }));
      return;
    }

    const res = await withToast(checkEmail(form.email), {
      success: '사용 가능한 이메일입니다.',
      error: '이미 사용 중인 이메일입니다.',
    });

    if (res) {
      setForm({ ...form, emailChecked: true });
      setErrors((prev) => ({ ...prev, email: '' }));
    } else {
      // ❌ 중복이면 초기화
      setForm({ ...form, email: '', emailChecked: false });
    }
  };

  const handleCheckNickname = async () => {
    if (!form.nickname) {
      setErrors((prev) => ({ ...prev, nickname: '닉네임을 입력하세요.' }));
      return;
    }

    if (form.nickname.length > 4) {
      setErrors((prev) => ({ ...prev, nickname: '닉네임은 최대 4글자까지 가능합니다.' }));
      return;
    }

    const res = await withToast(checkNickname(form.nickname), {
      success: '사용 가능한 닉네임입니다.',
      error: '이미 사용 중인 닉네임입니다.',
    });

    if (res) {
      setForm({ ...form, nicknameChecked: true });
      setErrors((prev) => ({ ...prev, nickname: '' }));
    } else {
      // ❌ 중복이면 초기화
      setForm({ ...form, nickname: '', nicknameChecked: false });
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    const res = await withToast(register(form), {
      success: '가입 완료! 관리자 승인 후 로그인 가능합니다.',
    });

    if (res) {
      navigate('/login');
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="max-w-md mx-auto mt-10 bg-white shadow-md rounded-lg p-8 space-y-6"
    >
      <h2 className="text-2xl font-bold text-center mb-6">회원가입</h2>

      {/* 이메일 */}
      <div>
        <label className="block text-sm font-medium mb-1">이메일</label>
        <div className="flex gap-2">
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            placeholder="이메일을 입력하세요"
            className={`flex-1 border rounded-md p-2 focus:ring-2 focus:outline-none ${
              errors.email
                ? 'border-red-500 focus:ring-red-500'
                : 'border-gray-300 focus:ring-blue-500'
            }`}
          />
          <button
            type="button"
            onClick={handleCheckEmail}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
          >
            중복확인
          </button>
        </div>
        {errors.email && <p className="text-sm text-red-500 mt-1">{errors.email}</p>}
      </div>

      {/* 닉네임 */}
      <div>
        <label className="block text-sm font-medium mb-1">닉네임</label>
        <div className="flex gap-2">
          <input
            type="text"
            name="nickname"
            value={form.nickname}
            onChange={handleChange}
            placeholder="닉네임을 입력하세요"
            className={`flex-1 border rounded-md p-2 focus:ring-2 focus:outline-none ${
              errors.nickname
                ? 'border-red-500 focus:ring-red-500'
                : 'border-gray-300 focus:ring-blue-500'
            }`}
          />
          <button
            type="button"
            onClick={handleCheckNickname}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
          >
            중복확인
          </button>
        </div>
        {errors.nickname && <p className="text-sm text-red-500 mt-1">{errors.nickname}</p>}
      </div>

      {/* 비밀번호 */}
      <div>
        <label className="block text-sm font-medium mb-1">비밀번호</label>
        <input
          type="password"
          name="password"
          value={form.password}
          onChange={handleChange}
          placeholder="비밀번호 입력"
          className={`w-full border rounded-md p-2 focus:ring-2 focus:outline-none ${
            errors.password
              ? 'border-red-500 focus:ring-red-500'
              : 'border-gray-300 focus:ring-blue-500'
          }`}
        />
        {errors.password && <p className="text-sm text-red-500 mt-1">{errors.password}</p>}
      </div>

      <div>
        <label className="block text-sm font-medium mb-1">비밀번호 확인</label>
        <input
          type="password"
          name="confirmPassword"
          value={form.confirmPassword}
          onChange={handleChange}
          placeholder="비밀번호 확인"
          className={`w-full border rounded-md p-2 focus:ring-2 focus:outline-none ${
            errors.confirmPassword
              ? 'border-red-500 focus:ring-red-500'
              : 'border-gray-300 focus:ring-blue-500'
          }`}
        />
        {errors.confirmPassword && (
          <p className="text-sm text-red-500 mt-1">{errors.confirmPassword}</p>
        )}
      </div>

      {/* 제출 버튼 */}
      <button
        type="submit"
        className="w-full py-3 bg-green-600 text-white font-medium rounded-md hover:bg-green-700 transition"
      >
        회원가입
      </button>
    </form>
  );
}
