import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fetchAdminPostDetail, deleteAdminPost } from '@/api/adminPostApi';
import type { PostDetailDTO } from '@/types/Post';
import { Viewer } from '@toast-ui/react-editor';
import ConfirmModal from '@/components/common/ConfirmModal';
import { withToast } from '@/utils/withToast';

export default function AdminPostDetail() {
  const { id } = useParams<{ id: string }>();
  const [post, setPost] = useState<PostDetailDTO | null>(null);
  const [openConfirm, setOpenConfirm] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      fetchAdminPostDetail(Number(id)).then(setPost).catch(console.error);
    }
  }, [id]);

  const confirmDelete = async () => {
    if (!id) return;

    await withToast(
      deleteAdminPost(Number(id)).then(() => navigate('/admin/posts')),
      { success: '게시글이 삭제되었습니다.', error: '삭제 중 오류 발생' },
    );

    setOpenConfirm(false);
  };

  if (!post) return <div className="p-6">로딩중...</div>;

  //const backendBaseUrl = 'http://localhost:8080';
  //const backendBaseUrl = import.meta.env.VITE_API_BASE_URL;
  const backendBaseUrl = import.meta.env.VITE_API_BASE_URL.replace(/\/api$/, '');

  return (
    <div className="p-6">
      {/* 제목 */}
      <h2 className="text-2xl font-bold mb-2">{post.title}</h2>

      {/* 작성자 + 작성일 */}
      <div className="text-sm text-gray-500 mb-4">
        {post.nickname} · {new Date(post.createdAt).toLocaleString('ko-KR')}
      </div>

      {/* 본문 */}
      <div className="border rounded-lg p-4 bg-white shadow mb-6">
        <Viewer initialValue={post.content.replace(/\/uploads\//g, `${backendBaseUrl}/uploads/`)} />
      </div>

      {/* ✅ 첨부파일 리스트 */}
      {post.attachments && post.attachments.length > 0 && (
        <div className="mb-6">
          <h3 className="font-semibold mb-2">첨부파일</h3>
          <ul className="space-y-1">
            {post.attachments.map((att) => (
              <li key={att.id}>
                <a
                  href={`${backendBaseUrl}/api/attachments/download/${att.id}`}
                  className="text-blue-600 hover:underline transition-colors"
                >
                  {att.originalName} ({att.fileSizeText})
                </a>
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* 버튼 영역 */}
      <div className="flex space-x-2">
        <button
          onClick={() => navigate('/admin/posts')}
          className="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300"
        >
          목록으로
        </button>
        <button
          onClick={() => setOpenConfirm(true)}
          className="px-4 py-2 rounded bg-red-500 text-white hover:bg-red-600"
        >
          삭제
        </button>
      </div>

      {/* 확인 모달 */}
      <ConfirmModal
        open={openConfirm}
        title="게시글 삭제"
        description="이 게시글을 삭제하시겠습니까?"
        onConfirm={confirmDelete}
        onCancel={() => setOpenConfirm(false)}
      />
    </div>
  );
}
