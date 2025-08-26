import { Outlet, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { logout } from '@store/slices/authSlice';
import type { AppDispatch } from '@store/index';
import { showToast } from '@store/slices/toastSlice';

export default function AdminLayout() {
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();

  const handleLogout = () => {
    // Redux 상태 초기화 + localStorage 정리
    dispatch(logout());
    dispatch(showToast({ message: '로그아웃 되었습니다.', type: 'success' }));

    // 로그인 화면으로 이동
    navigate('/login');
  };

  return (
    <div className="flex h-screen">
      {/* 사이드바 */}
      <aside className="w-64 bg-gray-800 text-white p-4">
        <h2 className="text-xl font-bold mb-6">Admin</h2>
        <nav>
          <ul className="space-y-2">
            <li className="hover:bg-gray-700 p-2 rounded cursor-pointer">대시보드</li>
            <li className="hover:bg-gray-700 p-2 rounded cursor-pointer">회원관리</li>
            <li className="hover:bg-gray-700 p-2 rounded cursor-pointer">통계</li>
          </ul>
        </nav>
      </aside>

      {/* 본문 영역 */}
      <div className="flex flex-col flex-1">
        {/* 헤더 */}
        <header className="flex items-center justify-between bg-white shadow p-4">
          <h1 className="text-lg font-semibold">관리자 페이지</h1>
          {/* ✅ 로그아웃 버튼 */}
          <button
            onClick={handleLogout}
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition"
          >
            로그아웃
          </button>
        </header>

        {/* Outlet (각 페이지 내용) */}
        <main className="flex-1 p-6 bg-gray-50">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
