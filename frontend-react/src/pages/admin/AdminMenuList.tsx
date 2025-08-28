import { useEffect, useState } from 'react';
import { Button } from '@/components/ui/Button';
import AdminMenuForm from '@/components/admin/AdminMenuForm';
import AdminMenuItem from '@/components/admin/AdminMenuItem';

import type { Menu } from '@/types/Menu';
import { fetchMenus, deleteMenu } from '@/api/adminMenuApi';

export default function AdminMenuList() {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [openForm, setOpenForm] = useState(false);
  const [editTarget, setEditTarget] = useState<Menu | null>(null);

  const loadMenus = async () => {
    const data = await fetchMenus();
    setMenus(data);
  };

  useEffect(() => {
    loadMenus();
  }, []);

  const handleAdd = () => {
    setEditTarget(null);
    setOpenForm(true);
  };

  const handleEdit = (menu: Menu) => {
    setEditTarget(menu);
    setOpenForm(true);
  };

  const handleDelete = async (id: number) => {
    await deleteMenu(id);
    setMenus((prev) => prev.filter((m) => m.id !== id));
  };

  return (
    <div className="p-4">
      <div className="flex justify-between mb-4">
        <h2 className="text-2xl font-bold">메뉴 관리</h2>
        <Button onClick={handleAdd}>+ 메뉴 추가</Button>
      </div>

      <table className="min-w-full border">
        <thead>
          <tr className="bg-gray-100">
            <th className="px-4 py-2 border">ID</th>
            <th className="px-4 py-2 border">이름</th>
            <th className="px-4 py-2 border">경로</th>
            <th className="px-4 py-2 border">권한</th>
            <th className="px-4 py-2 border">정렬</th>
            <th className="px-4 py-2 border">활성화</th>
            <th className="px-4 py-2 border">작업</th>
          </tr>
        </thead>
        <tbody>
          {menus.map((menu) => (
            <AdminMenuItem
              key={menu.id}
              menu={menu}
              onEdit={() => handleEdit(menu)}
              onDelete={() => handleDelete(menu.id)}
            />
          ))}
        </tbody>
      </table>

      {/* 추가/수정 폼 */}
      {openForm && (
        <AdminMenuForm
          menu={editTarget}
          menus={menus}
          onClose={() => setOpenForm(false)}
          onSuccess={loadMenus}
        />
      )}
    </div>
  );
}
