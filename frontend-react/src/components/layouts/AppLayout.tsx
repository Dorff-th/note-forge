// src/layouts/AppLayout.tsx
import { useSelector } from 'react-redux';
import PublicHeader from '@/components/layouts/PublicHeader';
import UserHeader from '@/components/layouts/UserHeader';
import type { RootState } from '@store/index';

export default function AppLayout({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useSelector((state: RootState) => state.auth);

  return (
    <div className="min-h-screen flex flex-col">
      {isAuthenticated ? <UserHeader /> : <PublicHeader />}
      <main className="flex-1 bg-gray-50">{children}</main>
    </div>
  );
}
