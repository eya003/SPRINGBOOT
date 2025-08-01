export interface Document {
  id?: number;
  title: string;
  content: string;
  fileName: string;
  fileType: string;
  category?: DocumentCategory;
  status?: ClassificationStatus;
  confidenceScore?: number;
  createdAt?: Date;
  updatedAt?: Date;
  userId: string;
}

export interface DocumentUploadRequest {
  title: string;
  content: string;
  fileName: string;
  fileType: string;
  userId: string;
}

export interface ClassificationResponse {
  predictedCategory: DocumentCategory;
  confidence: number;
  explanation: string;
}

export enum DocumentCategory {
  INVOICE = 'INVOICE',
  CONTRACT = 'CONTRACT',
  REPORT = 'REPORT',
  LETTER = 'LETTER',
  LEGAL = 'LEGAL',
  FINANCIAL = 'FINANCIAL',
  TECHNICAL = 'TECHNICAL',
  MARKETING = 'MARKETING',
  HR = 'HR',
  OTHER = 'OTHER'
}

export enum ClassificationStatus {
  PENDING = 'PENDING',
  PROCESSING = 'PROCESSING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  MANUAL_REVIEW = 'MANUAL_REVIEW'
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}