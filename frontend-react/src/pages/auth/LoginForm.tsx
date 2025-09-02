import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '@/api/axiosInstance';
import { loginSuccess } from '@store/slices/authSlice';
import { showToast } from '@store/slices/toastSlice';
import type { AppDispatch } from '@store/index';
import { jwtDecode } from 'jwt-decode';

// JWT payload 타입 정의
interface JwtPayload {
  sub: string; // username
  role: string; // ADMIN / USER
  exp: number; // 만료 시간
}

export default function LoginPage() {
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      // ✅ 로그인 API 호출 (token만 반환)
      const response = await axiosInstance.post('/auth/login', {
        username,
        password,
      });

      const { token, user } = response.data; // ✅ token만 추출
      const decoded: JwtPayload = jwtDecode(token); // ✅ payload 해석

      // payload에서 username, role 추출
      //const finalUsername = decoded.sub;
      const finalRole = decoded.role;

      // ✅ Redux store 업데이트
      //dispatch(loginSuccess({ token, username: finalUsername, role: finalRole }));

      dispatch(loginSuccess({ token: token, user }));

      // ✅ localStorage 저장
      localStorage.setItem('accessToken', token);
      localStorage.setItem('user', JSON.stringify(user));

      // ✅ 성공 토스트
      dispatch(showToast({ message: '로그인 성공!', type: 'success' }));

      // ✅ 권한별 리다이렉션
      if (finalRole === 'ROLE_ADMIN') {
        navigate('/admin');
      } else {
        navigate('/user');
      }
    } catch (error) {
      const err = error as any; // 👈 타입 단언
      console.error('로그인 실패:', err);

      let message = '로그인 실패! 아이디/비밀번호 확인';
      if (err.response?.data?.message) {
        message = err.response.data.message;
      }

      dispatch(showToast({ message, type: 'error' }));
    }
  };

  return (
    <div className="w-screen h-screen flex items-center justify-center bg-gray-100">
      <div className="w-full max-w-lg bg-white rounded-2xl shadow-2xl p-10">
        {/* 타이틀 */}
        <h1 className="text-3xl font-bold text-gray-800 text-center mb-10">NoteForge 로그인</h1>

        {/* 로그인 폼 */}
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Username */}
          <div>
            <label className="block text-sm font-medium text-gray-600 mb-1">아이디</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
              placeholder="아이디를 입력하세요"
              required
            />
          </div>

          {/* Password */}
          <div>
            <label className="block text-sm font-medium text-gray-600 mb-1">비밀번호</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 outline-none"
              placeholder="비밀번호를 입력하세요"
              required
            />
          </div>

          {/* 버튼 */}
          <button
            type="submit"
            className="w-full py-3 px-4 bg-indigo-600 text-white font-semibold rounded-lg shadow-md hover:bg-indigo-700 focus:ring-2 focus:ring-indigo-500 transition"
          >
            로그인
          </button>
        </form>

        {/* 하단 링크 */}
        <div className="flex justify-between text-sm text-gray-500 mt-8">
          <a href="#" className="hover:text-indigo-600">
            회원가입
          </a>
          <a href="#" className="hover:text-indigo-600">
            비밀번호 찾기
          </a>
        </div>
      </div>
    </div>
  );
}
