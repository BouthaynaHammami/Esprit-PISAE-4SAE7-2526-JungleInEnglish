// src/app/features/landing/landing.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ChildService } from '../../core/services/language/child.service';

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

  // ── Register flow ──────────────────────────────────────────────────────────
  registerStep: 'role' | 'form' | 'add-kids' = 'role';
  selectedRole: 'STUDENT' | 'TUTOR' | 'PARENT' = 'STUDENT';

  // Kids management (parent flow)
  kids: { name: string; birthDate: string }[] = [];
  newKidName = '';
  newKidBirthDate = '';
  kidsLoading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private childService: ChildService
  ) { }

  ngOnInit(): void {
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

    if (this.authService.isLoggedIn()) {
      this.authService.redirectByRole();
    }
  }

  // ── Modals ────────────────────────────────────────────────────────────────

  openStudentLogin(): void { this.resetMessages(); this.showStudentLoginModal = true; }
  openAdminLogin(): void { this.resetMessages(); this.showAdminLoginModal = true; }
  openRegister(): void {
    this.resetMessages();
    this.registerStep = 'role';
    this.selectedRole = 'STUDENT';
    this.kids = [];
    this.newKidName = '';
    this.newKidBirthDate = '';
    this.showRegisterModal = true;
  }

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

  // ── Role selection ────────────────────────────────────────────────────────

  selectRole(role: 'STUDENT' | 'TUTOR' | 'PARENT'): void {
    this.selectedRole = role;
    this.registerStep = 'form';
    this.resetMessages();
  }

  backToRoleSelect(): void {
    this.registerStep = 'role';
    this.resetMessages();
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
        // Only keep 'parent' flag if it was explicitly set during parent registration.
        // Do NOT auto-detect via children lookup — that caused students to see the parent dashboard
        // when the children service was slow or unavailable.
        // The flag is set in finishParentSetup() and cleared on logout/student-register.
        setTimeout(() => { this.closeAllModals(); this.authService.redirectByRole(); }, 500);
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
    const backendRole = this.selectedRole === 'PARENT' ? 'STUDENT' : this.selectedRole;

    this.authService.register({ ...payload, role: backendRole }).subscribe({
      next: () => {
        this.isLoading = false;
        if (this.selectedRole === 'PARENT') {
          this.successMessage = '✅ Account created! Now add your children.';
          this.registerStep = 'add-kids';
        } else {
          // Ensure no stale parent flag from a previous session
          localStorage.removeItem('devunity_user_type');
          this.successMessage = '✅ Account created! Redirecting…';
          setTimeout(() => {
            this.closeAllModals();
            this.authService.redirectByRole();
          }, 800);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.status === 409
          ? 'Email already in use.'
          : 'Registration failed. Please try again.';
      }
    });
  }

  // ── Kids (parent flow) ────────────────────────────────────────────────────

  addKidToList(): void {
    if (!this.newKidName.trim() || !this.newKidBirthDate) return;
    this.kids.push({ name: this.newKidName.trim(), birthDate: this.newKidBirthDate });
    this.newKidName = '';
    this.newKidBirthDate = '';
  }

  removeKid(index: number): void {
    this.kids.splice(index, 1);
  }

  finishParentSetup(): void {
    // Mark this account as a parent so we can show the parent UI on next login
    localStorage.setItem('devunity_user_type', 'parent');
    if (this.kids.length === 0) {
      this.closeAllModals();
      this.authService.redirectByRole();
      return;
    }
    this.kidsLoading = true;
    let saved = 0;
    const parentId = this.authService.getUserId() ?? 0;
    this.kids.forEach(kid => {
      this.childService.createChild({
        name: kid.name,
        age: this.calculateAge(kid.birthDate),
        avatar: '👶',
        parentId: parentId,
        xp: 0,
        level: 1
      }).subscribe({
        next: () => {
          saved++;
          if (saved === this.kids.length) {
            this.kidsLoading = false;
            this.closeAllModals();
            this.authService.redirectByRole();
          }
        },
        error: () => {
          saved++;
          if (saved === this.kids.length) {
            this.kidsLoading = false;
            this.closeAllModals();
            this.authService.redirectByRole();
          }
        }
      });
    });
  }

  private calculateAge(birthDate: string): number {
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const m = today.getMonth() - birth.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) age--;
    return Math.max(1, age);
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