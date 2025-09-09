import { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { Editor } from '@toast-ui/react-editor';
import axiosInstance from '@/api/axiosInstance';
import CategorySelect from './CategorySelect';
import PostTitleInput from './PostTitleInput';
import PostContentEditor from './PostContentEditor';
import PostTagInput from './PostTagInput';
import AttachmentUploader from './AttachmentUploader';
import FormActions from './FormActions';
import { showToast } from '@/store/slices/toastSlice'; // ✅ Redux 기반 Toast
import type { PostDetailDTO } from '@/types/Post';
import { fixContentForSave } from '@/utils/contentUrlHelper';

interface PostFormProps {
  mode: 'write' | 'edit';
  initialData?: PostDetailDTO;
}

export default function PostForm({ mode, initialData }: PostFormProps) {
  const editorRef = useRef<Editor>(null);

  const [categoryId, setCategoryId] = useState(initialData?.categoryId ?? 0);
  const [title, setTitle] = useState(initialData?.title ?? '');

  const [content, setContent] = useState(initialData?.content ?? '');

  const [tags, setTags] = useState<string[]>([]);
  const [attachments, setAttachments] = useState<File[]>([]);

  const navigate = useNavigate();
  const dispatch = useDispatch();

  // ✅ 최종 저장 핸들러
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const editorInstance = editorRef.current?.getInstance();
    if (!editorInstance) return;

    // 1. 본문 가져오기 (blob URL 포함 상태)
    let content = editorInstance.getMarkdown();

    // 2. blob URL 찾기
    const blobRegex = /!\[.*?\]\((blob:[^)]+)\)/g;
    const matches = [...content.matchAll(blobRegex)];

    for (const match of matches) {
      const blobUrl = match[1];

      try {
        // 3. blob 추출
        const blob = await fetch(blobUrl).then((r) => r.blob());
        const formData = new FormData();
        formData.append('image', blob, 'upload.png');

        // 4. 서버 업로드
        const { data } = await axiosInstance.post('/images/upload', formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });

        // 5. blob URL → 서버 URL 치환
        if (data?.url) {
          content = content.replace(blobUrl, data.url);
        }
      } catch (err) {
        console.error('이미지 업로드 중 오류:', err);
      }
    }

    content = fixContentForSave(content);

    const formData = new FormData();

    formData.append('title', title);
    formData.append('categoryId', String(categoryId));
    formData.append('content', content);
    formData.append('tags', tags.join(',')); // 서버에서 String → split 처리

    attachments.forEach((file) => {
      formData.append('attachments', file);
    });

    // 6. 게시글 저장
    try {
      let res;

      if (mode === 'write') {
        res = await axiosInstance.post('/posts', formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
      } else {
        const postId = initialData?.id;
        res = await axiosInstance.put(`/posts/${postId}`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' },
        });
      }

      const postId = res.data;
      if (postId) {
        dispatch(
          showToast({
            message:
              mode === 'write'
                ? '게시글이 성공적으로 등록되었습니다.'
                : '게시글이 성공적으로 수정되었습니다.',
            type: 'success',
            duration: 4000, // ✅ 상세 페이지로 이동 후에도 4초 유지
          }),
        );
        navigate(`/posts/${postId}`); // ✅ 신규 상세페이지로 이동
      } else {
        dispatch(
          showToast({
            message: '게시글 ID를 불러오지 못했습니다.',
            type: 'error',
          }),
        );
      }
    } catch (error) {
      console.error('게시글 등록 실패', error);
      dispatch(
        showToast({
          message:
            mode === 'write'
              ? '게시글 등록에 실패했습니다. 다시 시도해주세요.'
              : '게시글 수정에 실패했습니다. 다시 시도해주세요.',
          type: 'error',
        }),
      );
      // ✅ 머무르기 (navigate 없음)
    }
  }; // handleSubmit end

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <CategorySelect value={categoryId} onChange={setCategoryId} />
      <PostTitleInput value={title} onChange={setTitle} />
      <PostContentEditor ref={editorRef} value={content} onChange={setContent} />
      <PostTagInput value={tags} onChange={setTags} />
      <AttachmentUploader files={attachments} onChange={setAttachments} />
      <FormActions />
    </form>
  );
}
