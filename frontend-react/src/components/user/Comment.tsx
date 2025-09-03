import { useEffect, useState } from 'react';
import { fetchCommentsByPostId, deleteComments } from '@/api/adminPostApi';
import type { CommentResponse } from '@/types/Comment';
import { Button } from '@/components/ui/Button';
import { withToast } from '@/utils/withToast';
import ConfirmModal from '@/components/common/ConfirmModal';

interface AdminCommentProps {
  postId: number;
}

export default function Comment({ postId }: AdminCommentProps) {
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
      <h3 className="text-lg font-bold mb-2">Comments</h3>

      {/* 댓글 목록 테이블 */}
      <table className="w-full border text-sm">
        <thead>
          <tr className="bg-gray-100">
            <th className="p-2 border">ID</th>
            <th className="p-2 border">내용</th>
            <th className="p-2 border">작성자</th>
            <th className="p-2 border">작성일</th>
          </tr>
        </thead>
        <tbody>
          {comments.length > 0 ? (
            comments.map((c) => (
              <tr key={c.id}>
                <td className="p-2 border text-center">{c.id}</td>
                <td className="p-2 border">{c.content}</td>
                <td className="p-2 border">
                  {c.nickname} ({c.username})
                </td>
                <td className="p-2 border">{new Date(c.createdAt).toLocaleString()}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td className="p-2 border text-center" colSpan={5}>
                댓글이 없습니다.
              </td>
            </tr>
          )}
        </tbody>
      </table>

      {/* 삭제 확인 모달 */}
      {/* <ConfirmModal
        open={openConfirm}
        title="댓글 삭제"
        description={`${selectedIds.length}개의 댓글을 삭제하시겠습니까?`}
        onConfirm={handleDelete}
        onCancel={() => setOpenConfirm(false)}
      /> */}
    </div>
  );
}
