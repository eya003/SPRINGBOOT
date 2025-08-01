import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../../services/document.service';
import { Document, DocumentCategory, ClassificationStatus } from '../../models/document.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { DocumentDetailComponent } from '../document-detail/document-detail.component';

@Component({
  selector: 'app-document-list',
  templateUrl: './document-list.component.html',
  styleUrls: ['./document-list.component.css']
})
export class DocumentListComponent implements OnInit {
  documents: Document[] = [];
  filteredDocuments: Document[] = [];
  isLoading = false;
  selectedCategory: DocumentCategory | 'ALL' = 'ALL';
  selectedStatus: ClassificationStatus | 'ALL' = 'ALL';
  searchTerm = '';

  displayedColumns: string[] = ['title', 'fileName', 'category', 'status', 'confidenceScore', 'createdAt', 'actions'];

  categories = Object.values(DocumentCategory);
  statuses = Object.values(ClassificationStatus);

  constructor(
    private documentService: DocumentService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.loadDocuments();
  }

  loadDocuments(): void {
    this.isLoading = true;
    // In a real app, get userId from authentication service
    const userId = 'user123';
    
    this.documentService.getDocumentsByUser(userId).subscribe({
      next: (documents) => {
        this.documents = documents;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (error) => {
        this.snackBar.open('Error loading documents', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar']
        });
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    this.filteredDocuments = this.documents.filter(doc => {
      const matchesCategory = this.selectedCategory === 'ALL' || doc.category === this.selectedCategory;
      const matchesStatus = this.selectedStatus === 'ALL' || doc.status === this.selectedStatus;
      const matchesSearch = !this.searchTerm || 
        doc.title.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        doc.fileName.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      return matchesCategory && matchesStatus && matchesSearch;
    });
  }

  onCategoryChange(): void {
    this.applyFilters();
  }

  onStatusChange(): void {
    this.applyFilters();
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  viewDocument(document: Document): void {
    this.dialog.open(DocumentDetailComponent, {
      width: '800px',
      data: document
    });
  }

  classifyDocument(document: Document): void {
    if (document.id) {
      this.documentService.classifyDocument(document.id).subscribe({
        next: (response) => {
          this.snackBar.open(`Document classified as ${response.predictedCategory}`, 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
          this.loadDocuments();
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

  deleteDocument(document: Document): void {
    if (document.id && confirm('Are you sure you want to delete this document?')) {
      this.documentService.deleteDocument(document.id).subscribe({
        next: () => {
          this.snackBar.open('Document deleted successfully', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
          this.loadDocuments();
        },
        error: (error) => {
          this.snackBar.open('Error deleting document', 'Close', {
            duration: 3000,
            panelClass: ['error-snackbar']
          });
        }
      });
    }
  }

  getStatusColor(status: ClassificationStatus): string {
    switch (status) {
      case ClassificationStatus.COMPLETED:
        return 'primary';
      case ClassificationStatus.PROCESSING:
        return 'accent';
      case ClassificationStatus.FAILED:
        return 'warn';
      default:
        return '';
    }
  }

  getCategoryColor(category: DocumentCategory): string {
    const colors = ['primary', 'accent', 'warn'];
    const index = Object.values(DocumentCategory).indexOf(category);
    return colors[index % colors.length];
  }
}