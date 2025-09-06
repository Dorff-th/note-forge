import { useEffect, useState } from 'react';
import { getMyProfile } from '@/api/userApi';
import type { UserProfile } from '@/types/User';
import { Settings } from 'lucide-react';
import ProfileImageModal from '@/components/user/ProfileImageModal';
import { backendBaseUrl } from '@/config';

export default function UserProfilePage() {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);

  async function fetchProfile() {
    try {
      const data = await getMyProfile();
      setProfile(data);
    } catch (err) {
      console.error('프로필 조회 실패:', err);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    fetchProfile();
  }, []);

  if (loading) {
    return <p className="text-center mt-10">로딩중...</p>;
  }

  if (!profile) {
    return <p className="text-center mt-10 text-red-500">프로필 정보를 불러올 수 없습니다.</p>;
  }

  return (
    <div className="max-w-2xl mx-auto mt-10">
      <div className="bg-white shadow-lg rounded-xl p-8">
        <h2 className="text-2xl font-bold mb-8 border-b pb-4">내 프로필</h2>

        <div className="flex items-center gap-6 mb-8 relative">
          {/* 아바타 */}
          {profile.profileImageUrl ? (
            <img
              src={
                profile.profileImageUrl
                  ? `${backendBaseUrl}${profile.profileImageUrl}`
                  : '/default.png'
              }
              alt="프로필 이미지"
              className="w-24 h-24 rounded-full object-cover ring-2 ring-indigo-200"
            />
          ) : (
            <div className="w-24 h-24 rounded-full bg-gray-200 flex items-center justify-center text-gray-500 text-sm">
              No Image
            </div>
          )}

          {/* 톱니바퀴 아이콘 */}
          <button
            onClick={() => setIsModalOpen(true)}
            className="absolute top-0 left-20 bg-white rounded-full p-1 shadow hover:bg-gray-50"
          >
            <Settings size={20} className="text-gray-600" />
          </button>

          <div>
            <p className="text-xl font-semibold">{profile.nickname}</p>
            <p className="text-gray-500">{profile.username}</p>
          </div>
        </div>

        {/* 프로필 정보 */}
        <div className="grid grid-cols-2 gap-y-4 text-sm">
          <div className="font-medium text-gray-600">역할</div>
          <div className="text-gray-800">{profile.role}</div>

          {profile.createdAt && (
            <>
              <div className="font-medium text-gray-600">가입일</div>
              <div>{new Date(profile.createdAt).toLocaleDateString()}</div>
            </>
          )}

          {profile.updatedAt && (
            <>
              <div className="font-medium text-gray-600">마지막 수정</div>
              <div>{new Date(profile.updatedAt).toLocaleDateString()}</div>
            </>
          )}
        </div>
      </div>

      {/* 모달 */}
      <ProfileImageModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSuccess={fetchProfile} // 업로드/삭제 후 갱신
      />
    </div>
  );
}
