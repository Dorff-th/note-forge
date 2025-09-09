import { forwardRef, useEffect } from 'react';
import { Editor } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor.css';
import { fixContentForEditor } from '@/utils/contentUrlHelper';

interface Props {
  value: string;
  onChange: (val: string) => void;
}

// ✅ forwardRef 사용 → 부모에서 ref로 제어 가능
const PostContentEditor = forwardRef<Editor, Props>(({ value, onChange }, ref) => {
  // ref는 부모(PostForm)에서 주입됨

  useEffect(() => {
    if (!ref || typeof ref === 'function') return;
    const editorInstance = ref.current?.getInstance();
    if (!editorInstance) return;

    // 📌 신규 작성/수정 공통: 본문을 있는 그대로 세팅
    if (value) {
      const fixedContent = fixContentForEditor(value);
      editorInstance.setMarkdown(fixedContent);
    }

    // 📌 신규 작성에서만 addImageBlobHook → blob:// 임시 미리보기
    editorInstance.addHook(
      'addImageBlobHook',
      async (blob: Blob, callback: (url: string, altText: string) => void) => {
        const tempUrl = URL.createObjectURL(blob);
        callback(tempUrl, (blob as File).name);
        return false;
      },
    );
  }, [ref, value]);

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
