// src/utils/contentUrlHelper.ts
import { backendBaseUrl } from '@/config';

/**
 * ✅ DB에서 가져온 content를 Editor에 표시하기 위해 절대경로로 변환
 * ex) ![img](/uploads/a.png) → ![img](http://localhost:8080/uploads/a.png)
 */
export function fixContentForEditor(content: string): string {
  if (!content) return content;

  return content.replace(/!\[(.*?)\]\((\/uploads\/.*?)\)/g, (match, alt, path) => {
    return `![${alt}](${backendBaseUrl}${path})`;
  });
}

/**
 * ✅ Editor에서 작성된 content를 DB에 저장하기 위해 상대경로로 변환
 * ex) ![img](http://localhost:8080/uploads/a.png) → ![img](/uploads/a.png)
 */
export function fixContentForSave(content: string): string {
  if (!content) return content;

  const regex = new RegExp(`!\\[(.*?)\\]\\(${backendBaseUrl}(\\/uploads\\/.*?)\\)`, 'g');
  return content.replace(regex, (match, alt, path) => {
    return `![${alt}](${path})`;
  });
}
