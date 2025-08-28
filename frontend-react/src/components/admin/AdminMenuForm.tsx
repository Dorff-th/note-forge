import { useState } from 'react';
import { Button } from '@/components/ui/Button';
import type { Menu } from '@/types/Menu';
import { RoleType } from '@/types/Role';
import { createMenu, updateMenu } from '@/api/adminMenuApi';

interface AdminMenuFormProps {
  menu: Menu | null; // null이면 추가 모드, 있으면 수정 모드
  menus: Menu[]; // 부모 메뉴 선택용 전체 목록
  onClose: () => void;
  onSuccess: () => void;
}

export default function AdminMenuForm({ menu, menus, onClose, onSuccess }: AdminMenuFormProps) {
  const [formData, setFormData] = useState<Omit<Menu, 'id'>>({
    parentId: menu?.parentId ?? undefined,
    name: menu?.name ?? '',
    path: menu?.path ?? '',
    role: menu?.role ?? RoleType.PUBLIC,
    sortOrder: menu?.sortOrder ?? 0,
    active: menu?.active ?? true,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;

    let newValue: any = value;
    if (name === 'active') {
      newValue = value === 'true';
    }
    if (name === 'sortOrder') {
      newValue = Number(value);
    }
    if (name === 'parentId') {
      newValue = value === '' ? undefined : Number(value);
    }

    setFormData((prev) => ({
      ...prev,
      [name]: newValue,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (menu) {
      await updateMenu(menu.id, formData);
    } else {
      await createMenu(formData);
    }

    onSuccess();
    onClose();
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
      <div className="bg-white rounded-lg shadow-lg p-6 w-[500px]">
        <h3 className="text-xl font-bold mb-4">{menu ? '메뉴 수정' : '메뉴 추가'}</h3>

        <form onSubmit={handleSubmit} className="space-y-4">
          {/* 이름 */}
          <div>
            <label className="block text-sm font-medium">이름</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className="mt-1 block w-full border rounded p-2"
              required
            />
          </div>

          {/* 경로 */}
          <div>
            <label className="block text-sm font-medium">경로</label>
            <input
              type="text"
              name="path"
              value={formData.path}
              onChange={handleChange}
              className="mt-1 block w-full border rounded p-2"
              required
            />
          </div>

          {/* 권한 */}
          <div>
            <label className="block text-sm font-medium">권한</label>
            <select
              name="role"
              value={formData.role}
              onChange={handleChange}
              className="mt-1 block w-full border rounded p-2"
            >
              {Object.values(RoleType).map((role) => (
                <option key={role} value={role}>
                  {role}
                </option>
              ))}
            </select>
          </div>

          {/* 부모 메뉴 */}
          <div>
            <label className="block text-sm font-medium">부모 메뉴</label>
            <select
              name="parentId"
              value={formData.parentId ?? ''}
              onChange={handleChange}
              className="mt-1 block w-full border rounded p-2"
            >
              <option value="">최상위</option>
              {menus.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.name}
                </option>
              ))}
            </select>
          </div>

          {/* 정렬 순서 */}
          <div>
            <label className="block text-sm font-medium">정렬 순서</label>
            <input
              type="number"
              name="sortOrder"
              value={formData.sortOrder}
              onChange={handleChange}
              className="mt-1 block w-full border rounded p-2"
            />
          </div>

          {/* 활성화 여부 */}
          <div>
            <label className="block text-sm font-medium">활성화 여부</label>
            <select
              name="active"
              value={formData.active ? 'true' : 'false'}
              onChange={handleChange}
              className="mt-1 block w-full border rounded p-2"
            >
              <option value="true">활성</option>
              <option value="false">비활성</option>
            </select>
          </div>

          <div className="flex justify-end space-x-2 pt-4">
            <Button type="button" variant="outline" onClick={onClose}>
              취소
            </Button>
            <Button type="submit">{menu ? '수정' : '추가'}</Button>
          </div>
        </form>
      </div>
    </div>
  );
}
