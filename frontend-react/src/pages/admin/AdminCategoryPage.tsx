import { useEffect, useState } from 'react';
import {
  fetchCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from '@/api/adminCategoryApi';
import type { Category } from '@/types/Category';
import { Button } from '@/components/ui/Button';
import ConfirmModal from '@/components/common/ConfirmModal';
import { withToast } from '@/utils/withToast';

export default function AdminCategoryPage() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [newCategory, setNewCategory] = useState('');
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editingName, setEditingName] = useState('');

  const loadCategories = async () => {
    const data = await fetchCategories();
    setCategories(data);
  };

  useEffect(() => {
    loadCategories();
  }, []);

  const handleAdd = async () => {
    if (!newCategory.trim()) return;
    await createCategory(newCategory.trim());
    setNewCategory('');
    loadCategories();
  };

  const handleUpdate = async (id: number) => {
    if (!editingName.trim()) return;
    await updateCategory(id, editingName.trim());
    setEditingId(null);
    setEditingName('');
    loadCategories();
  };

  const [open, setOpen] = useState(false);
  const handleDelete = async (id: number) => {
    await withToast(
      deleteCategory(id).then(() => loadCategories()),
      { success: '카테고리 삭제 완료' },
    );
  };

  const handleConfirm = (id: number) => {
    handleDelete(id); // ✅ 이제 여기서 실제 삭제 실행
    setOpen(false);
  };

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">카테고리 관리</h2>

      {/* 추가 */}
      <div className="flex mb-4 gap-2">
        <input
          type="text"
          value={newCategory}
          onChange={(e) => setNewCategory(e.target.value)}
          placeholder="새 카테고리 이름"
          className="border rounded px-2 py-1 flex-1"
        />
        <Button onClick={handleAdd}>추가</Button>
      </div>

      {/* 목록 */}
      <table className="w-full border">
        <thead>
          <tr className="bg-gray-100">
            <th className="border px-2 py-1">ID</th>
            <th className="border px-2 py-1">이름</th>
            <th className="border px-2 py-1">작업</th>
          </tr>
        </thead>
        <tbody>
          {categories.map((cat) => (
            <tr key={cat.id}>
              <td className="border px-2 py-1">{cat.id}</td>
              <td className="border px-2 py-1">
                {editingId === cat.id ? (
                  <input
                    type="text"
                    value={editingName}
                    onChange={(e) => setEditingName(e.target.value)}
                    className="border rounded px-2 py-1 w-full"
                  />
                ) : (
                  cat.name
                )}
              </td>
              <td className="border px-2 py-1 space-x-2">
                {editingId === cat.id ? (
                  <>
                    <Button onClick={() => handleUpdate(cat.id)}>저장</Button>
                    <Button variant="secondary" onClick={() => setEditingId(null)}>
                      취소
                    </Button>
                  </>
                ) : (
                  <>
                    <Button
                      variant="secondary"
                      onClick={() => {
                        setEditingId(cat.id);
                        setEditingName(cat.name);
                      }}
                    >
                      수정
                    </Button>
                    <Button variant="destructive" onClick={() => setOpen(true)}>
                      삭제
                    </Button>
                    <ConfirmModal
                      open={open}
                      title="카테고리 삭제"
                      description={`삭제 시 "${cat.name}" 카테고리의 글은 기본 카테고리로 이동합니다. 계속하시겠습니까?`}
                      onConfirm={() => handleConfirm(cat.id)}
                      onCancel={() => setOpen(false)}
                    />
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
