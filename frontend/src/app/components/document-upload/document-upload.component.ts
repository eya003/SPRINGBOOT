import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DocumentService } from '../../services/document.service';
import { DocumentUploadRequest } from '../../models/document.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-document-upload',
  templateUrl: './document-upload.component.html',
  styleUrls: ['./document-upload.component.css']
})
export class DocumentUploadComponent {
  uploadForm: FormGroup;
  isUploading = false;

  constructor(
    private fb: FormBuilder,
    private documentService: DocumentService,
    private snackBar: MatSnackBar
  ) {
    this.uploadForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(10)]],
      fileName: ['', Validators.required],
      fileType: ['text/plain', Validators.required],
      userId: ['user123', Validators.required] // In a real app, this would come from authentication
    });
  }

  onSubmit(): void {
    if (this.uploadForm.valid) {
      this.isUploading = true;
      const request: DocumentUploadRequest = this.uploadForm.value;

      this.documentService.uploadDocument(request).subscribe({
        next: (document) => {
          this.snackBar.open('Document uploaded successfully!', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
          this.uploadForm.reset();
          this.uploadForm.patchValue({ userId: 'user123', fileType: 'text/plain' });
          this.isUploading = false;
        },
        error: (error) => {
          this.snackBar.open('Error uploading document. Please try again.', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar']
          });
          this.isUploading = false;
        }
      });
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.uploadForm.patchValue({
        fileName: file.name,
        fileType: file.type || 'text/plain'
      });

      // Read file content for text files
      if (file.type.startsWith('text/') || file.name.endsWith('.txt')) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.uploadForm.patchValue({
            content: e.target?.result as string
          });
        };
        reader.readAsText(file);
      }
    }
  }
}