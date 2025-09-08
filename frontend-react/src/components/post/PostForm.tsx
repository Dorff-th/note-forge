import { useRef, useState } from 'react';
import { Editor } from '@toast-ui/react-editor';
import axiosInstance from '@/api/axiosInstance';
import CategorySelect from './CategorySelect';
import PostTitleInput from './PostTitleInput';
import PostContentEditor from './PostContentEditor';
import PostTagInput from './PostTagInput';
import AttachmentUploader from './AttachmentUploader';
import FormActions from './FormActions';

export default function PostForm() {
  const editorRef = useRef<Editor>(null);
  const [title, setTitle] = useState('');
  const [categoryId, setCategoryId] = useState<number | undefined>(undefined);

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

    console.log('blob matches:', matches);

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

        console.log('업로드 응답:', data);
        console.log('업로드 응답:', data?.url);

        // 5. blob URL → 서버 URL 치환
        if (data?.url) {
          content = content.replace(blobUrl, data.url);
        }
      } catch (err) {
        console.error('이미지 업로드 중 오류:', err);
      }
    }

    console.log('최종 content:', content);

    // 6. 게시글 저장
    await axiosInstance.post('/posts', {
      title,
      categoryId,
      content,
    });
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <CategorySelect value={categoryId} onChange={setCategoryId} />
      <PostTitleInput value={title} onChange={setTitle} />
      <PostContentEditor ref={editorRef} />
      <PostTagInput />
      <AttachmentUploader />
      <FormActions />
    </form>
  );
}
