import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Document, DocumentCategory } from '../../models/document.model';
import { DocumentService } from '../../services/document.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-document-detail',
  templateUrl: './document-detail.component.html',
  styleUrls: ['./document-detail.component.css']
})
export class DocumentDetailComponent {
  categories = Object.values(DocumentCategory);
  selectedCategory: DocumentCategory;

  constructor(
    public dialogRef: MatDialogRef<DocumentDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public document: Document,
    private documentService: DocumentService,
    private snackBar: MatSnackBar
  ) {
    this.selectedCategory = document.category || DocumentCategory.OTHER;
  }

  onClose(): void {
    this.dialogRef.close();
  }

  updateCategory(): void {
    if (this.document.id && this.selectedCategory !== this.document.category) {
      this.documentService.updateDocumentCategory(this.document.id, this.selectedCategory).subscribe({
        next: (updatedDocument) => {
          this.document.category = updatedDocument.category;
          this.snackBar.open('Category updated successfully', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
        },
        error: (error) => {
          this.snackBar.open('Error updating category', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar']
          });
        }
      });
    }
  }

  classifyDocument(): void {
    if (this.document.id) {
      this.documentService.classifyDocument(this.document.id).subscribe({
        next: (response) => {
          this.document.category = response.predictedCategory;
          this.document.confidenceScore = response.confidence;
          this.selectedCategory = response.predictedCategory;
          this.snackBar.open(`Document classified as ${response.predictedCategory}`, 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
        },
        error: (error) => {
          this.snackBar.open('Error classifying document', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar']
          });
        }
      });
    }
  }
}