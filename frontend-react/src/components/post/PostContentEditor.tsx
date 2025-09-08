import { forwardRef, useEffect } from 'react';
import { Editor } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor.css';

// ✅ forwardRef 사용 → 부모에서 ref로 제어 가능
const PostContentEditor = forwardRef<Editor, {}>((props, ref) => {
  // ref는 부모(PostForm)에서 주입됨

  useEffect(() => {
    // ref가 Editor에 연결된 이후 hook 등록
    if (!ref || typeof ref === 'function') return;

    const editorInstance = ref.current?.getInstance();
    if (!editorInstance) return;

    // ✅ 이미지 업로드 Hook (로컬 미리보기만)
    editorInstance.addHook(
      'addImageBlobHook',
      async (blob: Blob, callback: (url: string, altText: string) => void) => {
        try {
          const file = blob as File;
          const tempUrl = URL.createObjectURL(file);

          callback(tempUrl, file.name); // altText = 파일명
        } catch (err) {
          console.error('이미지 미리보기 실패:', err);
        }
        return false; // 기본 업로드 막음
      },
    );
  }, [ref]);

  return (
    <div className="border rounded-lg overflow-hidden">
      <Editor
        ref={ref}
        height="400px"
        initialEditType="markdown"
        previewStyle="vertical"
        placeholder="내용을 작성하세요..."
        useCommandShortcut={true}
      />
    </div>
  );
});

PostContentEditor.displayName = 'PostContentEditor';

export default PostContentEditor;
