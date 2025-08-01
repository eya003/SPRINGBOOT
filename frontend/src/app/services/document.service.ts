import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Document, DocumentUploadRequest, ClassificationResponse, DocumentCategory, PageResponse } from '../models/document.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private apiUrl = 'http://localhost:8080/api/documents';

  constructor(private http: HttpClient) { }

  uploadDocument(request: DocumentUploadRequest): Observable<Document> {
    return this.http.post<Document>(this.apiUrl, request);
  }

  getDocument(id: number): Observable<Document> {
    return this.http.get<Document>(`${this.apiUrl}/${id}`);
  }

  getDocumentsByUser(userId: string): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/user/${userId}`);
  }

  getDocumentsByUserPaginated(
    userId: string, 
    page: number = 0, 
    size: number = 10, 
    sortBy: string = 'createdAt', 
    sortDir: string = 'desc'
  ): Observable<PageResponse<Document>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PageResponse<Document>>(`${this.apiUrl}/user/${userId}/paginated`, { params });
  }

  getDocumentsByCategory(category: DocumentCategory): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/category/${category}`);
  }

  getDocumentsByUserAndCategory(userId: string, category: DocumentCategory): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/user/${userId}/category/${category}`);
  }

  updateDocumentCategory(id: number, category: DocumentCategory): Observable<Document> {
    return this.http.put<Document>(`${this.apiUrl}/${id}/category`, category);
  }

  classifyDocument(id: number): Observable<ClassificationResponse> {
    return this.http.post<ClassificationResponse>(`${this.apiUrl}/${id}/classify`, {});
  }

  deleteDocument(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}