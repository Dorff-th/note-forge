import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { fetchMemberDetail, updateMemberStatus, updateMemberDeleted } from '@/api/adminMemberApi';
import type { MemberDetail } from '@/types/Member';
import { Button } from '@/components/ui/Button';

export default function AdminMemberDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [member, setMember] = useState<MemberDetail | null>(null);

  const loadDetail = async () => {
    if (id) {
      const data = await fetchMemberDetail(Number(id));
      setMember(data);
    }
  };

  useEffect(() => {
    loadDetail();
  }, [id]);

  const handleStatusToggle = async () => {
    if (!member) return;
    const newStatus = member.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    await updateMemberStatus(member.id, newStatus);
    loadDetail();
  };

  const handleDeleteToggle = async () => {
    if (!member) return;
    await updateMemberDeleted(member.id, !member.deleted);
    loadDetail();
  };

  if (!member) return <p className="p-4">로딩 중...</p>;

  return (
    <div className="p-6">
      <div className="bg-white rounded-2xl shadow-md p-6">
        <h2 className="text-2xl font-bold mb-6 text-gray-800 border-b pb-3">회원 상세</h2>

        <div className="grid grid-cols-2 gap-x-6 gap-y-3 text-gray-700">
          <p>
            <span className="font-semibold">ID:</span> {member.id}
          </p>
          <p>
            <span className="font-semibold">Username:</span> {member.username}
          </p>
          <p>
            <span className="font-semibold">Email:</span> {member.email}
          </p>
          <p>
            <span className="font-semibold">Nickname:</span> {member.nickname}
          </p>
          <p>
            <span className="font-semibold">Role:</span> {member.role}
          </p>
          <p>
            <span className="font-semibold">Status:</span>{' '}
            <span className={member.status === 'ACTIVE' ? 'text-green-600' : 'text-red-600'}>
              {member.status}
            </span>
          </p>
          <p>
            <span className="font-semibold">Deleted:</span>{' '}
            {member.deleted ? (
              <span className="text-red-600">탈퇴</span>
            ) : (
              <span className="text-blue-600">정상</span>
            )}
          </p>
          <p>
            <span className="font-semibold">Created At:</span> {member.createdAt}
          </p>
          <p>
            <span className="font-semibold">Updated At:</span> {member.updatedAt || '-'}
          </p>
        </div>

        <div className="mt-8 flex space-x-3">
          <Button variant="outline" onClick={() => navigate(-1)}>
            목록으로
          </Button>
          <Button variant="default" onClick={handleStatusToggle}>
            {member.status === 'ACTIVE' ? '차단하기' : '활성화하기'}
          </Button>
          <Button variant={member.deleted ? 'default' : 'destructive'} onClick={handleDeleteToggle}>
            {member.deleted ? '복구하기' : '탈퇴처리'}
          </Button>
        </div>
      </div>
    </div>
  );
}
