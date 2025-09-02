// src/types/Attachment.ts
export interface Attachment {
  id: number;
  fileName: string;
  originalName: string;
  fileUrl: string; // S3나 로컬 저장소 URL
  fileSizeText: string;
  createdAt: string;
}
