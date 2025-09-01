import { useEffect, useState } from 'react';
import { fetchAdminPosts, deleteAdminPosts } from '@/api/adminPostApi';
import type { PostDTO } from '@/types/Post';
import type { PageResponse } from '@/types/Common';
import Pagination from '@/components/common/Pagination';
import { useNavigate } from 'react-router-dom';
import ConfirmModal from '@/components/common/ConfirmModal';
import { withToast } from '@/utils/withToast';

export default function AdminPostList() {
  const [posts, setPosts] = useState<PostDTO[]>([]);
  const [pageInfo, setPageInfo] = useState<PageResponse<PostDTO> | null>(null);
  const [page, setPage] = useState(1);
  const navigate = useNavigate();
  const [selectedIds, setSelectedIds] = useState<number[]>([]);
  const [openConfirm, setOpenConfirm] = useState(false);

  const loadPosts = async (pageNum: number) => {
    const data = await withToast(fetchAdminPosts(pageNum, 10, 'createdAt', 'DESC'), {
      error: '게시글 목록 로드 실패',
    });
    if (data) {
      setPosts(data.dtoList);
      setPageInfo(data);
      setPage(pageNum);
      setSelectedIds([]);
    }
  };

  useEffect(() => {
    loadPosts(1);
  }, []);

  // 체크박스 선택/해제
  const toggleSelect = (id: number) => {
    setSelectedIds((prev) => (prev.includes(id) ? prev.filter((v) => v !== id) : [...prev, id]));
  };

  // 전체 선택/해제
  const toggleSelectAll = () => {
    if (posts.length === selectedIds.length) {
      setSelectedIds([]);
    } else {
      setSelectedIds(posts.map((p) => p.id));
    }
  };

  // 선택 삭제
  const handleDeleteSelected = () => {
    if (selectedIds.length === 0) {
      withToast(Promise.reject(), { error: '삭제할 게시글을 선택하세요.' });
      return;
    }
    setOpenConfirm(true);
  };

  const confirmDelete = async () => {
    await withToast(
      deleteAdminPosts(selectedIds).then(() => loadPosts(page)),
      { success: `${selectedIds.length}개 게시글이 삭제되었습니다.`, error: '삭제 중 오류 발생' },
    );
    setOpenConfirm(false);
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">게시글 목록 (Admin)</h2>

      {/* 삭제 버튼 */}
      <div className="mb-3">
        <button
          onClick={handleDeleteSelected}
          className="px-4 py-2 rounded bg-red-500 text-white hover:bg-red-600"
        >
          선택 삭제
        </button>
      </div>

      {/* 📌 게시글 테이블 */}
      <div className="overflow-x-auto bg-white rounded-lg shadow">
        <table className="min-w-full text-sm text-left text-gray-600">
          <thead className="bg-gray-100 text-gray-700 uppercase text-xs">
            <tr>
              <th className="px-4 py-3">
                <input
                  type="checkbox"
                  checked={posts.length > 0 && selectedIds.length === posts.length}
                  onChange={toggleSelectAll}
                />
              </th>
              <th className="px-4 py-3">ID</th>
              <th className="px-4 py-3">제목</th>
              <th className="px-4 py-3">닉네임</th>
              <th className="px-4 py-3">카테고리</th>
              <th className="px-4 py-3">댓글</th>
              <th className="px-4 py-3">첨부파일</th>
              <th className="px-4 py-3">작성일</th>
            </tr>
          </thead>
          <tbody>
            {posts.length > 0 ? (
              posts.map((post) => (
                <tr key={post.id} className="border-b hover:bg-gray-50">
                  <td className="px-4 py-2 text-center">
                    <input
                      type="checkbox"
                      checked={selectedIds.includes(post.id)}
                      onChange={() => toggleSelect(post.id)}
                    />
                  </td>
                  <td className="px-4 py-2">{post.id}</td>
                  <td
                    className="px-4 py-2 font-medium text-blue-600 cursor-pointer"
                    onClick={() => navigate(`/admin/posts/${post.id}`)}
                  >
                    {post.title}
                  </td>
                  <td className="px-4 py-2">{post.nickname}</td>
                  <td className="px-4 py-2">{post.categoryName}</td>
                  <td className="px-4 py-2 text-center">{post.commentCount}</td>
                  <td className="px-4 py-2 text-center">{post.attachmentCount}</td>
                  <td className="px-4 py-2">
                    {new Date(post.createdAt).toLocaleDateString('ko-KR')}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={8} className="px-4 py-6 text-center text-gray-400">
                  게시글이 없습니다.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* 📌 페이징 컴포넌트 */}
      {pageInfo && (
        <Pagination
          page={page}
          startPage={pageInfo.startPage}
          endPage={pageInfo.endPage}
          prev={pageInfo.prev}
          next={pageInfo.next}
          onPageChange={loadPosts}
        />
      )}

      {/* 확인 모달 */}
      <ConfirmModal
        open={openConfirm}
        title="게시글 삭제"
        description={`${selectedIds.length}개 게시글을 삭제하시겠습니까?`}
        onConfirm={confirmDelete}
        onCancel={() => setOpenConfirm(false)}
      />
    </div>
  );
}
