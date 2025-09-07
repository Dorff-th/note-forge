import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '@/pages/auth/LoginForm';
import AdminLayout from '@/components/layouts/AdminLayout';
import { useAppSelector } from '@store/hooks';
import AdminMenuList from '@/pages/admin/AdminMenuList';
import AdminCategoryPage from '@/pages/admin/AdminCategoryPage';
import AdminStatsPage from '@/pages/admin/AdminStatsPage';
import AdminMemberListPage from '@/pages/admin/AdminMemberListPage';
import AdminMemberDetailPage from '@/pages/admin/AdminMemberDetailPage';
import UserHomePage from '@/pages/user/UserHomePage';
import AdminPostList from '@/pages/admin/AdminPostList';
import AdminPostDetail from '@/pages/admin/AdminPostDetail';
import AppLayout from '@/components/layouts/AppLayout';
import UserHeader from '@/components/layouts/UserHeader';
import PostListPage from '@/pages/post/PostListPage';
import PostDetail from '@/pages/post/PostDetail';
import TagPostListPage from '@/pages/post/TagPostListPage';
import SearchPage from '@/pages/search/SearchPage';
import UserProfilePage from '@/pages/user/UserProfilePage';
import UserRegisterPage from '@/pages/user/UserRegisterPage';

const AppRouter = () => {
  const { token, user } = useAppSelector((state) => state.auth);

  return (
    <Routes>
      <Route
        path="/posts"
        element={
          <AppLayout>
            <PostListPage />
          </AppLayout>
        }
      />

      <Route
        path="/posts/tags/:tagName"
        element={
          <AppLayout>
            <TagPostListPage />
          </AppLayout>
        }
      />

      <Route
        path="/search"
        element={
          <AppLayout>
            <SearchPage />
          </AppLayout>
        }
      />

      <Route
        path="/posts/:id"
        element={
          <AppLayout>
            <PostDetail />
          </AppLayout>
        }
      />

      {/* 로그인 */}
      <Route path="/login" element={<LoginPage />} />

      {/* 사용자 등록 */}
      <Route path="/register" element={<UserRegisterPage />} />

      <Route
        path="/user"
        element={
          token && user?.role === 'ROLE_USER' ? <UserHeader /> : <Navigate to="/posts" replace />
        }
      >
        {/* 사용자 홈 */}
        <Route path="home" element={<UserHomePage />} />
        <Route path="profile" element={<UserProfilePage />} />
      </Route>

      {/* ADMIN 라우트 */}
      <Route
        path="/admin"
        element={
          token && user?.role === 'ROLE_ADMIN' ? <AdminLayout /> : <Navigate to="/posts" replace />
        }
      >
        <Route index element={<AdminStatsPage />} />
        <Route path="menus" element={<AdminMenuList />} />
        <Route path="categories" element={<AdminCategoryPage />} />
        <Route path="stats" element={<AdminStatsPage />} />
        <Route path="members" element={<AdminMemberListPage />} />
        <Route path="/admin/members/:id" element={<AdminMemberDetailPage />} />
        <Route path="posts" element={<AdminPostList />} />
        <Route path="/admin/posts/:id" element={<AdminPostDetail />} />
      </Route>

      {/* 기본 리다이렉션 */}
      {/* <Route path="*" element={<Navigate to="/login" />} /> */}
      <Route path="*" element={<Navigate to="/posts" />} />
    </Routes>
  );
};

export default AppRouter;
