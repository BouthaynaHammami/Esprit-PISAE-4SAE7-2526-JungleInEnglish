import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-student-dropout-form',
  templateUrl: './student-dropout-form.component.html',
  styleUrls: ['./student-dropout-form.component.css']
})
export class StudentDropoutFormComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  loading = false;
  success = false;
  error: string | null = null;
  prediction: any = null;

  learningModes = [
    { value: 'online', label: 'Online' },
    { value: 'in-person', label: 'In-Person' },
    { value: 'hybrid', label: 'Hybrid' }
  ];

  englishLevels = [
    { value: 'A2', label: 'A2 - Elementary' },
    { value: 'B1', label: 'B1 - Intermediate' },
    { value: 'B2', label: 'B2 - Upper Intermediate' },
    { value: 'C1', label: 'C1 - Advanced' },
    { value: 'C2', label: 'C2 - Mastery' }
  ];

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  private initializeForm(): void {
    this.form = this.fb.group({
      motivation_level: [5, [Validators.required, Validators.min(1), Validators.max(10)]],
      weekly_study_hours: [5, [Validators.required, Validators.min(0)]],
      free_time_hours_per_week: [10, [Validators.required, Validators.min(0)]],
      satisfaction_level: [5, [Validators.required, Validators.min(1), Validators.max(10)]],
      preferred_learning_mode: ['online', Validators.required],
      attendance_commitment: [7, [Validators.required, Validators.min(1), Validators.max(10)]],
      homework_completion_self: [7, [Validators.required, Validators.min(1), Validators.max(10)]],
      financial_stress_level: [3, [Validators.required, Validators.min(1), Validators.max(10)]],
      interaction_with_teacher: [6, [Validators.required, Validators.min(1), Validators.max(10)]],
      english_level_self: ['A2', Validators.required],
      goal_clarity_level: [7, [Validators.required, Validators.min(1), Validators.max(10)]],
      class_difficulty_level: [5, [Validators.required, Validators.min(1), Validators.max(10)]],
      peer_interaction_level: [6, [Validators.required, Validators.min(1), Validators.max(10)]],
      technical_issues_frequency: [2, [Validators.required, Validators.min(1), Validators.max(10)]]
    });
  }

  get f() {
    return this.form.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    this.error = null;

    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    this.http.post('http://localhost:8081/learners/api/dropout-forms', this.form.value).subscribe({
      next: (response: any) => {
        this.prediction = response;
        this.success = true;
        this.loading = false;
        setTimeout(() => {
          this.router.navigate(['/student/dashboard']);
        }, 3000);
      },
      error: (err: any) => {
        this.error = err.error?.message || 'Failed to submit form. Please try again.';
        this.loading = false;
      }
    });
  }

  resetForm(): void {
    this.form.reset();
    this.submitted = false;
    this.initializeForm();
  }
}
