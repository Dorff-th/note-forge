// src/components/post/AttachmentUploader.tsx
export default function AttachmentUploader() {
  return (
    <div>
      <label className="block text-sm font-medium mb-2">첨부파일</label>
      <input
        type="file"
        multiple
        className="block w-full text-sm text-gray-600 border rounded-md cursor-pointer focus:ring focus:ring-blue-200"
      />
      {/* 첨부 미리보기 샘플 */}
      <ul className="mt-2 space-y-1 text-sm text-gray-700">
        <li className="flex justify-between items-center border rounded-md px-2 py-1">
          example.pdf
          <button type="button" className="text-red-500 hover:underline text-xs">
            삭제
          </button>
        </li>
      </ul>
    </div>
  );
}
