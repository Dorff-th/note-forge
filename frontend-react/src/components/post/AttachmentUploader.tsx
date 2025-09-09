import { useState } from 'react';
import { formatFileSize } from '@/utils/fileUtils';

interface AttachmentUploaderProps {
  files: File[];
  onChange: (files: File[]) => void;
}

export default function AttachmentUploader({ files, onChange }: AttachmentUploaderProps) {
  const [error, setError] = useState<string | null>(null);

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selectedFiles = e.target.files ? Array.from(e.target.files) : [];
    if (!selectedFiles.length) return;

    // 기존 + 신규 합산
    const totalFiles = [...files, ...selectedFiles];
    if (totalFiles.length > 3) {
      setError('첨부파일은 최대 3개까지만 가능합니다.');
      return;
    }

    setError(null);
    onChange(totalFiles);
  };

  const handleRemove = (index: number) => {
    const newFiles = files.filter((_, i) => i !== index);
    onChange(newFiles);
  };

  return (
    <div className="space-y-2">
      <label className="block font-semibold text-gray-700">첨부파일</label>
      <input
        type="file"
        multiple
        onChange={handleFileSelect}
        className="block w-full border rounded px-2 py-1 text-sm"
      />

      {error && <p className="text-red-500 text-sm">{error}</p>}

      <ul className="space-y-1">
        {files.map((file, index) => (
          <li key={index} className="flex items-center justify-between text-sm border p-2 rounded">
            <span>
              {file.name} <span className="text-gray-500">({formatFileSize(file.size)})</span>
            </span>
            <button
              type="button"
              onClick={() => handleRemove(index)}
              className="text-red-500 hover:underline"
            >
              삭제
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
