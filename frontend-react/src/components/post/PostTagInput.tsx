import { useState } from 'react';
import { fetchSuggest } from '@/api/tagApi';

interface PostTagInputProps {
  value: string[];
  onChange: (tags: string[]) => void;
}

export default function PostTagInput({ value, onChange }: PostTagInputProps) {
  const [inputValue, setInputValue] = useState('');
  const [suggestions, setSuggestions] = useState<string[]>([]);

  // 🔹 입력값 변경
  const handleInputChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const val = e.target.value;
    setInputValue(val);

    if (val.trim().length >= 2) {
      const result = await fetchSuggest(val);
      setSuggestions(result);
    } else {
      setSuggestions([]);
    }
  };

  // 🔹 태그 추가 (# 제거 후 저장)
  const addTag = (tag: string) => {
    if (!tag) return;

    const trimmed = tag.replace(/^#/, ''); // # 제거
    if (!value.includes(trimmed)) {
      onChange([...value, trimmed]); // 상태에는 순수 문자열만 저장
    }
    setInputValue('');
    setSuggestions([]);
  };

  // 🔹 Enter/Comma 처리
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' || e.key === ',') {
      e.preventDefault();
      addTag(inputValue.trim());
    }
  };

  // 🔹 태그 삭제
  const removeTag = (tag: string) => {
    onChange(value.filter((t) => t !== tag));
  };

  return (
    <div className="space-y-2">
      <label className="block text-sm font-medium text-gray-700">태그</label>
      <div className="flex flex-wrap gap-2 border rounded p-2">
        {/* 태그칩 (표시할 때만 # 붙임) */}
        {value.map((tag) => (
          <span key={tag} className="flex items-center bg-gray-200 rounded px-2 py-1 text-sm">
            #{tag}
            <button
              type="button"
              onClick={() => removeTag(tag)}
              className="ml-1 text-gray-500 hover:text-red-500"
            >
              ×
            </button>
          </span>
        ))}

        {/* 입력창 */}
        <input
          type="text"
          value={inputValue}
          onChange={handleInputChange}
          onKeyDown={handleKeyDown}
          className="flex-1 outline-none"
          placeholder="태그 입력 (Enter 또는 ,)"
        />
      </div>

      {/* 자동완성 */}
      {suggestions.length > 0 && (
        <ul className="border rounded bg-white shadow mt-1 max-h-40 overflow-y-auto">
          {suggestions.map((s) => (
            <li
              key={s}
              onClick={() => addTag(s)}
              className="px-3 py-1 hover:bg-gray-100 cursor-pointer"
            >
              {s}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
