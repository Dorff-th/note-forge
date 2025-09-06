// src/components/user/ProfileImageModal.tsx
import { useState } from 'react';
import { uploadProfileImage, deleteProfileImage } from '@/api/userApi';
import { X } from 'lucide-react';
import { withToast } from '@/utils/withToast';
import { useDispatch } from 'react-redux';
import { updateUser } from '@/store/slices/authSlice';
//import type { RootState } from '@/store';

interface ProfileImageModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void; // 업로드/삭제 성공 시 프로필 새로고침
}

export default function ProfileImageModal({ isOpen, onClose, onSuccess }: ProfileImageModalProps) {
  const [file, setFile] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);

  const dispatch = useDispatch();
  if (!isOpen) return null;

  const handleUpload = async () => {
    if (!file) return;
    setLoading(true);
    try {
      const response = await withToast(uploadProfileImage(file), {
        success: '프로필 이미지가 업로드되었습니다.',
        error: '업로드에 실패했습니다.',
      });

      if (response) {
        dispatch(updateUser({ profileImageUrl: response.profileImageUrl }));
      }

      onSuccess();
      onClose();
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    setLoading(true);
    try {
      await withToast(deleteProfileImage(), {
        success: '프로필 이미지가 삭제되었습니다.',
        error: '삭제에 실패했습니다.',
      });

      // 🔹 Redux 상태 갱신 (profileImageUrl을 null로 초기화)
      dispatch(updateUser({ profileImageUrl: null }));
      onSuccess();
      onClose();
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
      <div className="bg-white rounded-lg shadow-lg w-full max-w-md p-6 relative">
        {/* 닫기 버튼 */}
        <button
          onClick={onClose}
          className="absolute top-3 right-3 text-gray-400 hover:text-gray-600"
        >
          <X size={20} />
        </button>

        <h3 className="text-lg font-semibold mb-4">프로필 이미지 관리</h3>

        {/* 파일 업로드 */}
        <input
          type="file"
          accept="image/*"
          onChange={(e) => setFile(e.target.files?.[0] || null)}
          className="mb-4"
        />

        <div className="flex justify-end gap-2">
          <button
            onClick={handleUpload}
            disabled={!file || loading}
            className="px-4 py-2 rounded-md bg-indigo-600 text-white hover:bg-indigo-700 disabled:opacity-50"
          >
            업로드
          </button>
          <button
            onClick={handleDelete}
            disabled={loading}
            className="px-4 py-2 rounded-md bg-red-500 text-white hover:bg-red-600 disabled:opacity-50"
          >
            삭제
          </button>
        </div>
      </div>
    </div>
  );
}
