import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchMembers, updateMemberStatus, updateMemberDeleted } from '@/api/adminMemberApi';
import type { MemberResult } from '@/types/Member';
import { Button } from '@/components/ui/Button';
import { Tabs, TabsList, TabsTrigger, TabsContent } from '@/components/ui/tabs';

export default function AdminMemberListPage() {
  const [members, setMembers] = useState<MemberResult[]>([]);
  const [activeTab, setActiveTab] = useState<'active' | 'inactive'>('active');

  const loadMembers = async (tab: 'active' | 'inactive') => {
    const data = await fetchMembers({ tab });
    setMembers(data);
  };

  useEffect(() => {
    loadMembers(activeTab);
  }, [activeTab]);

  const handleStatusToggle = async (id: number, status: 'ACTIVE' | 'INACTIVE') => {
    const newStatus = status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    await updateMemberStatus(id, newStatus);
    loadMembers(activeTab);
  };

  const handleDeleteToggle = async (id: number, deleted: boolean) => {
    await updateMemberDeleted(id, !deleted);
    loadMembers(activeTab);
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-6">회원 관리</h2>

      <Tabs value={activeTab} onValueChange={(val) => setActiveTab(val as 'active' | 'inactive')}>
        {/* 탭 버튼 */}
        <TabsList>
          <TabsTrigger value="active">활성 회원</TabsTrigger>
          <TabsTrigger value="inactive">비활성/탈퇴 회원</TabsTrigger>
        </TabsList>

        {/* 탭 내용 */}
        <TabsContent value="active" className="mt-4">
          <MemberTable
            members={members}
            onStatusToggle={handleStatusToggle}
            onDeleteToggle={handleDeleteToggle}
          />
        </TabsContent>

        <TabsContent value="inactive" className="mt-4">
          <MemberTable
            members={members}
            onStatusToggle={handleStatusToggle}
            onDeleteToggle={handleDeleteToggle}
          />
        </TabsContent>
      </Tabs>
    </div>
  );
}

/* 🔹 테이블 컴포넌트 분리 */
function MemberTable({
  members,
  onStatusToggle,
  onDeleteToggle,
}: {
  members: MemberResult[];
  onStatusToggle: (id: number, status: 'ACTIVE' | 'INACTIVE') => void;
  onDeleteToggle: (id: number, deleted: boolean) => void;
}) {
  return (
    <table className="w-full border-collapse border text-sm">
      <thead>
        <tr className="bg-gray-100 text-left">
          <th className="border p-2">ID</th>
          <th className="border p-2">Username</th>
          <th className="border p-2">Email</th>
          <th className="border p-2">Role</th>
          <th className="border p-2">Status</th>
          <th className="border p-2">Deleted</th>
          <th className="border p-2">액션</th>
        </tr>
      </thead>
      <tbody>
        {members.length === 0 ? (
          <tr>
            <td colSpan={7} className="p-4 text-center text-gray-500">
              회원이 없습니다.
            </td>
          </tr>
        ) : (
          members.map((m) => (
            <tr key={m.id}>
              <td className="border p-2">{m.id}</td>
              <td className="border p-2">
                <Link to={`/admin/members/${m.id}`} className="text-blue-600 hover:underline">
                  {m.username}
                </Link>
              </td>
              <td className="border p-2">{m.email}</td>
              <td className="border p-2">{m.role}</td>
              <td className="border p-2">
                <span className={m.status === 'ACTIVE' ? 'text-green-600' : 'text-red-600'}>
                  {m.status}
                </span>
              </td>
              <td className="border p-2">{m.deleted ? '탈퇴' : '정상'}</td>
              <td className="border p-2 space-x-2">
                <Button variant="outline" onClick={() => onStatusToggle(m.id, m.status)}>
                  {m.status === 'ACTIVE' ? '차단' : '활성'}
                </Button>
                <Button variant="destructive" onClick={() => onDeleteToggle(m.id, m.deleted)}>
                  {m.deleted ? '복구' : '삭제'}
                </Button>
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}
