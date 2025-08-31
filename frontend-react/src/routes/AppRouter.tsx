import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '@/pages/auth/LoginForm';
import AdminLayout from '@/components/layouts/AdminLayout';
import { useAppSelector } from '@store/hooks';
import AdminMenuList from '@/pages/admin/AdminMenuList';
import AdminCategoryPage from '@/pages/admin/AdminCategoryPage';
import AdminStatsPage from '@/pages/admin/AdminStatsPage';
import AdminMemberListPage from '@/pages/admin/AdminMemberListPage';
import AdminMemberDetailPage from '@/pages/admin/AdminMemberDetailPage';

const AppRouter = () => {
  const { token, role } = useAppSelector((state) => state.auth);

  return (
    <Routes>
      {/* 로그인 */}
      <Route path="/login" element={<LoginPage />} />

      {/* ADMIN 라우트 */}
      <Route
        path="/admin"
        element={
          token && role === 'ROLE_ADMIN' ? <AdminLayout /> : <Navigate to="/login" replace />
        }
      >
        <Route index element={<AdminStatsPage />} />
        <Route path="menus" element={<AdminMenuList />} />
        <Route path="categories" element={<AdminCategoryPage />} />
        <Route path="stats" element={<AdminStatsPage />} />
        <Route path="members" element={<AdminMemberListPage />} />
        <Route path="/admin/members/:id" element={<AdminMemberDetailPage />} />
      </Route>

      {/* USER 라우트 (추후 확장 가능) */}
      {/* <Route
        path="/user"
        element={
          token && role === 'ROLE_USER' ? (
            <UserLayout />
          ) : (
            <Navigate to="/login" replace />
          )
        }
      /> */}

      {/* 기본 리다이렉션 */}
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>
  );
};

export default AppRouter;
