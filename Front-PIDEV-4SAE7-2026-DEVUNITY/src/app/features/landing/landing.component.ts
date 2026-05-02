// src/app/features/landing/landing.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent implements OnInit {

  showStudentLoginModal = false;
  showAdminLoginModal = false;
  showRegisterModal = false;

  loginForm!: FormGroup;
  adminLoginForm!: FormGroup;
  registerForm!: FormGroup;

  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService) { }

  ngOnInit(): void {
    // Always initialize forms first — template binds to them unconditionally
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    this.adminLoginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    this.registerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });

    // Redirect after forms are ready so template never sees undefined FormGroups
    if (this.authService.isLoggedIn()) {
      this.authService.redirectByRole();
    }
  }

  // ── Modals ────────────────────────────────────────────────────────────────

  openStudentLogin(): void { this.resetMessages(); this.showStudentLoginModal = true; }
  openAdminLogin(): void { this.resetMessages(); this.showAdminLoginModal = true; }
  openRegister(): void { this.resetMessages(); this.showRegisterModal = true; }

  closeAllModals(): void {
    this.showStudentLoginModal = false;
    this.showAdminLoginModal = false;
    this.showRegisterModal = false;
    this.resetMessages();
  }

  switchToRegister(): void {
    this.closeAllModals();
    setTimeout(() => this.openRegister(), 50);
  }

  switchToLogin(): void {
    this.closeAllModals();
    setTimeout(() => this.openStudentLogin(), 50);
  }

  // ── Submissions ───────────────────────────────────────────────────────────

  onStudentLogin(): void {
    if (this.loginForm.invalid) { this.loginForm.markAllAsTouched(); return; }
    this.isLoading = true;
    this.resetMessages();

    this.authService.login(this.loginForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = '✅ Login successful! Redirecting…';
        setTimeout(() => {
          this.closeAllModals();
          this.authService.redirectByRole();
        }, 800);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.status === 403 || err.status === 401
          ? 'Invalid email or password.'
          : 'An error occurred. Please try again.';
      }
    });
  }

  onAdminLogin(): void {
    if (this.adminLoginForm.invalid) { this.adminLoginForm.markAllAsTouched(); return; }
    this.isLoading = true;
    this.resetMessages();

    this.authService.login(this.adminLoginForm.value).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = '✅ Admin login successful! Redirecting…';
        setTimeout(() => {
          this.closeAllModals();
          this.authService.redirectByRole();
        }, 800);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.status === 403 || err.status === 401
          ? 'Invalid credentials or insufficient permissions.'
          : 'An error occurred. Please try again.';
      }
    });
  }

  onRegister(): void {
    if (this.registerForm.invalid) { this.registerForm.markAllAsTouched(); return; }
    this.isLoading = true;
    this.resetMessages();

    const { confirmPassword: _, ...payload } = this.registerForm.value;

    this.authService.register({ ...payload, role: 'STUDENT' }).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = '✅ Account created! Redirecting…';
        setTimeout(() => {
          this.closeAllModals();
          this.authService.redirectByRole();
        }, 800);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.status === 409
          ? 'Email already in use.'
          : 'Registration failed. Please try again.';
      }
    });
  }

  // ── Template Helpers ──────────────────────────────────────────────────────

  fieldInvalid(form: FormGroup, field: string): boolean {
    if (!form) return false;
    const ctrl = form.get(field);
    return !!(ctrl && ctrl.invalid && ctrl.touched);
  }

  get passwordMismatch(): boolean {
    return !!(
      this.registerForm?.errors?.['passwordMismatch'] &&
      this.registerForm.get('confirmPassword')?.touched
    );
  }

  // ── Field Error Accessor ──────────────────────────────────────────────────

  getFieldError(form: FormGroup, field: string): string {
    if (!form) return '';
    const ctrl = form.get(field);
    if (!ctrl || !ctrl.touched || !ctrl.errors) return '';
    if (ctrl.errors['required']) return 'This field is required.';
    if (ctrl.errors['email']) return 'Please enter a valid email address.';
    if (ctrl.errors['minlength']) return `Minimum ${ctrl.errors['minlength'].requiredLength} characters required.`;
    return 'Invalid value.';
  }

  // ── Private ───────────────────────────────────────────────────────────────

  private resetMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  private passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const pw = group.get('password')?.value;
    const cpw = group.get('confirmPassword')?.value;
    return pw === cpw ? null : { passwordMismatch: true };
  }
}