import { useEffect, useState } from 'react';
import { fetchCommentsByPostId, deleteComments } from '@/api/adminPostApi';
import type { CommentResponse } from '@/types/Comment';
import Avatar from '@/components/common/Avatar';
import { Button } from '@/components/ui/Button';
import { withToast } from '@/utils/withToast';
import ConfirmModal from '@/components/common/ConfirmModal';

interface UserCommentProps {
  postId: number;
}

export default function UserComment({ postId }: UserCommentProps) {
  const [comments, setComments] = useState<CommentResponse[]>([]);
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const [openConfirm, setOpenConfirm] = useState(false);

  // 댓글 목록 불러오기
  const loadComments = async () => {
    const data = await fetchCommentsByPostId(postId);
    setComments(data);
  };

  useEffect(() => {
    loadComments();
  }, [postId]);

  return (
    <div className="mt-6">
      <h3 className="text-lg font-bold mb-4">댓글 목록</h3>

      <div className="space-y-3">
        {comments.length > 0 ? (
          comments.map((c) => (
            <div
              key={c.id}
              className="p-4 border rounded-md bg-white hover:shadow-md transition flex gap-3"
            >
              {/* 프로필 */}
              <Avatar src={c.profileImageUrl} alt={c.nickname} size={40} />

              {/* 댓글 내용 */}
              <div className="flex-1">
                <div className="flex items-center justify-between mb-1">
                  <span className="font-medium text-gray-700">
                    {c.nickname} ({c.username})
                  </span>
                  <span className="text-sm text-gray-500">
                    {new Date(c.createdAt).toLocaleString()}
                  </span>
                </div>

                <p className="text-gray-800">{c.content}</p>
              </div>
            </div>
          ))
        ) : (
          <div className="p-4 text-center text-gray-500 border rounded-md bg-gray-50">
            댓글이 없습니다.
          </div>
        )}
      </div>
    </div>
  );
}
