import type { Menu } from '@/types/Menu';
import { Button } from '@/components/ui/Button';

interface AdminMenuItemProps {
  menu: Menu;
  onEdit: () => void;
  onDelete: () => void;
}

export default function AdminMenuItem({ menu, onEdit, onDelete }: AdminMenuItemProps) {
  return (
    <tr key={menu.id} className="hover:bg-gray-50">
      <td className="px-4 py-2 border">{menu.id}</td>
      <td className="px-4 py-2 border">{menu.name}</td>
      <td className="px-4 py-2 border">{menu.path}</td>
      <td className="px-4 py-2 border">{menu.role}</td>
      <td className="px-4 py-2 border text-center">{menu.sortOrder}</td>
      <td className="px-4 py-2 border">
        {menu.active ? (
          <span className="text-green-600 font-semibold">활성</span>
        ) : (
          <span className="text-red-500 font-semibold">비활성</span>
        )}
      </td>
      <td className="px-4 py-2 border space-x-2 text-center">
        <Button size="sm" variant="outline" onClick={onEdit}>
          수정
        </Button>
        <Button
          size="sm"
          variant="destructive"
          onClick={() => {
            if (window.confirm('정말 삭제하시겠습니까?')) {
              onDelete();
            }
          }}
        >
          삭제
        </Button>
      </td>
    </tr>
  );
}
