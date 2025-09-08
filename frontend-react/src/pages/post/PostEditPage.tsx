// src/pages/PostEditPage.tsx
import PostForm from '@/components/post/PostForm';

export default function PostEditPage() {
  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      <h1 className="text-2xl font-bold mb-6">게시글 수정</h1>
      <PostForm />
    </div>
  );
}
