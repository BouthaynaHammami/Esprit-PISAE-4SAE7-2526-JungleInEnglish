import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface DropoutFormData {
  id: number;
  userId: number;
  userEmail: string;
  motivationLevel: number;
  weeklyStudyHours: number;
  freeTimeHoursPerWeek: number;
  satisfactionLevel: number;
  preferredLearningMode: string;
  attendanceCommitment: number;
  homeworkCompletionSelf: number;
  financialStressLevel: number;
  interactionWithTeacher: number;
  englishLevelSelf: string;
  goalClarityLevel: number;
  classDifficultyLevel: number;
  peerInteractionLevel: number;
  technicalIssuesFrequency: number;
  predictedDropout: string;
  predictedProbability: number;
  modelName: string;
  createdAt: string;
}

@Component({
  selector: 'app-admin-dropout-forms',
  templateUrl: './admin-dropout-forms.component.html',
  styleUrls: ['./admin-dropout-forms.component.css']
})
export class AdminDropoutFormsComponent implements OnInit {
  forms: DropoutFormData[] = [];
  filteredForms: DropoutFormData[] = [];
  loading = true;
  error: string | null = null;

  filterStatus = 'all'; // all, yes, no
  searchEmail = '';
  sortBy: 'date' | 'email' | 'status' = 'date';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadForms();
  }

  loadForms(): void {
    this.loading = true;
    this.error = null;

    this.http.get<DropoutFormData[]>('http://localhost:8081/learners/api/dropout-forms').subscribe({
      next: (data) => {
        this.forms = data;
        this.applyFiltersAndSort();
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load forms. Please try again.';
        this.loading = false;
        console.error(err);
      }
    });
  }

  applyFiltersAndSort(): void {
    let filtered = [...this.forms];

    // Apply status filter
    if (this.filterStatus !== 'all') {
      filtered = filtered.filter(f => 
        (this.filterStatus === 'yes' && f.predictedDropout === 'yes') ||
        (this.filterStatus === 'no' && f.predictedDropout === 'no')
      );
    }

    // Apply email filter
    if (this.searchEmail.trim()) {
      filtered = filtered.filter(f =>
        f.userEmail.toLowerCase().includes(this.searchEmail.toLowerCase())
      );
    }

    // Apply sorting
    filtered.sort((a, b) => {
      if (this.sortBy === 'date') {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      } else if (this.sortBy === 'email') {
        return a.userEmail.localeCompare(b.userEmail);
      } else if (this.sortBy === 'status') {
        return a.predictedDropout.localeCompare(b.predictedDropout);
      }
      return 0;
    });

    this.filteredForms = filtered;
  }

  onFilterChange(): void {
    this.applyFiltersAndSort();
  }

  onSearchChange(): void {
    this.applyFiltersAndSort();
  }

  onSortChange(): void {
    this.applyFiltersAndSort();
  }

  getRiskClass(status: string): string {
    return status === 'yes' ? 'risk-high' : 'risk-low';
  }

  getRiskLabel(status: string): string {
    return status === 'yes' ? '⚠️ At Risk' : '✅ On Track';
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatProbability(prob: number): string {
    return (prob * 100).toFixed(1) + '%';
  }

  exportToCSV(): void {
    const headers = [
      'Email',
      'Status',
      'Probability',
      'Motivation',
      'Study Hours',
      'Satisfaction',
      'Learning Mode',
      'Attendance',
      'English Level',
      'Date'
    ];

    const rows = this.filteredForms.map(f => [
      f.userEmail,
      f.predictedDropout,
      this.formatProbability(f.predictedProbability),
      f.motivationLevel,
      f.weeklyStudyHours,
      f.satisfactionLevel,
      f.preferredLearningMode,
      f.attendanceCommitment,
      f.englishLevelSelf,
      this.formatDate(f.createdAt)
    ]);

    let csvContent = headers.join(',') + '\n';
    rows.forEach(row => {
      csvContent += row.map(cell => `"${cell}"`).join(',') + '\n';
    });

    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `dropout-forms-${new Date().toISOString().split('T')[0]}.csv`;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}
