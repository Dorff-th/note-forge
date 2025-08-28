import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '@/pages/auth/LoginForm';
import AdminLayout from '@/components/layouts/AdminLayout';
import { useAppSelector } from '@store/hooks';
import AdminDashboardPage from '@/pages/admin/AdminDashboardPage';
import AdminMenuList from '@/pages/admin/AdminMenuList';

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
        <Route index element={<AdminDashboardPage />} />
        <Route path="menus" element={<AdminMenuList />} />
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
