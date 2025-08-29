import { Outlet, useNavigate, Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { logout } from '@store/slices/authSlice';
import type { AppDispatch } from '@store/index';
import { showToast } from '@store/slices/toastSlice';
import { useEffect, useState } from 'react';
import axiosInstance from '@/api/axiosInstance';
import type { MenuTree } from '@/types/MenuTree';
import { ChevronRight, ChevronDown } from 'lucide-react';

// ✅ 메뉴 아이템 컴포넌트
function MenuItem({ menu, depth }: { menu: MenuTree; depth: number }) {
  const [open, setOpen] = useState(true);
  const children = menu.children ?? [];
  const hasChildren = children.length > 0;

  return (
    <li>
      <div
        className={`
          flex items-center justify-between 
          p-2 rounded cursor-pointer
          hover:bg-gray-700
          ${depth === 0 ? 'font-semibold' : 'text-sm text-gray-300'}
          ${depth > 0 ? 'pl-6' : 'pl-2'}
        `}
        onClick={() => hasChildren && setOpen(!open)}
      >
        <Link to={menu.path} className="flex-1">
          {menu.name}
        </Link>

        {hasChildren && (
          <span className="ml-2">
            {open ? (
              <ChevronDown size={16} className="text-gray-400" />
            ) : (
              <ChevronRight size={16} className="text-gray-400" />
            )}
          </span>
        )}
      </div>

      {hasChildren && open && (
        <ul className="mt-1 space-y-1">
          {children.map((child) => (
            <MenuItem key={child.id} menu={child} depth={depth + 1} />
          ))}
        </ul>
      )}
    </li>
  );
}

export default function AdminLayout() {
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();
  const [menus, setMenus] = useState<MenuTree[]>([]);

  useEffect(() => {
    axiosInstance
      .get<MenuTree[]>('/menus')
      .then((res) => setMenus(res.data))
      .catch(() => {
        dispatch(showToast({ message: '메뉴 불러오기 실패', type: 'error' }));
      });
  }, [dispatch]);

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
      <aside className="w-48 bg-gray-800 text-white p-4">
        <h2 className="text-xl font-bold mb-6">Admin</h2>
        <nav>
          <ul className="space-y-2">
            {menus.map((menu) => (
              <MenuItem key={menu.id} menu={menu} depth={0} />
            ))}
          </ul>
        </nav>
      </aside>

      {/* 본문 영역 */}
      <div className="flex flex-col flex-1 w-full">
        {/* 헤더 */}
        <header className="flex items-center justify-between bg-white shadow p-4">
          <h1 className="text-lg font-semibold">관리자 페이지</h1>
          <button
            onClick={handleLogout}
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition"
          >
            로그아웃
          </button>
        </header>

        {/* Outlet (각 페이지 내용) */}
        <main className="flex-1 p-6 bg-gray-50">
          <div className="max-w-full mx-auto">
            <Outlet />
          </div>
        </main>
      </div>
    </div>
  );
}
