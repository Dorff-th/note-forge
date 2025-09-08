// src/components/post/PostTagInput.tsx
export default function PostTagInput() {
  return (
    <div>
      <label className="block text-sm font-medium mb-2">태그</label>
      <input
        type="text"
        placeholder="태그를 입력하고 엔터를 눌러 추가하세요"
        className="w-full border rounded-md px-3 py-2 focus:ring focus:ring-blue-200"
      />
      {/* 태그 리스트 샘플 */}
      <div className="flex gap-2 mt-2">
        <span className="px-2 py-1 bg-gray-100 rounded-md text-sm">#React</span>
        <span className="px-2 py-1 bg-gray-100 rounded-md text-sm">#Spring</span>
      </div>
    </div>
  );
}
