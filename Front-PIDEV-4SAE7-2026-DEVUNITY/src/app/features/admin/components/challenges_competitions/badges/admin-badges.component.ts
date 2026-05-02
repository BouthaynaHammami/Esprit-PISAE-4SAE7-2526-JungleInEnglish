import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { Badge } from '../../../../../core/models/challenges-competitions.model';
import { BadgeService } from '../../../../../core/services/activity/badge.service';

type Mode = 'LIST' | 'FORM';

@Component({
  selector: 'app-badges',
  templateUrl: './admin-badges.component.html',
  styleUrls: ['./admin-badges.component.scss']
})
export class AdminBadgesComponent {
  // UI state
  mode: Mode = 'LIST';
  showInlineForm = false;
  loading = false;
  error: string | null = null;

  // data
  all: Badge[] = [];
  filtered: Badge[] = [];

  // filters (ngModel)
  nameQuery = '';
  pointsFilter: '' | '0-50' | '51-100' | '101-200' | '200+' = '';

  // form state
  isEdit = false;
  editingId: number | null = null;

  form!: FormGroup;

  constructor(
    private badgeService: BadgeService,
    private fb: FormBuilder
  ) {

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      description: [null as string | null],
      pointsRequired: [0, [Validators.required, Validators.min(0)]],
      imageUrl: [null as string | null],
    });
  }

  // Détermine la classe d'accent pour la carte en fonction des points
  badgeClass(b: Badge): string {
    const p = b.pointsRequired ?? 0;
    if (p <= 100) return 'accent-secondary';
    if (p <= 199) return 'accent-coral';
    return 'accent-primary';
  }


  ngOnInit(): void {
    this.loadAll();
  }

  // =========================
  // LOAD
  // =========================
  loadAll(): void {
    this.loading = true;
    this.error = null;

    this.badgeService.getAll().subscribe({
      next: (data) => {
        // tri: pointsRequired asc
        this.all = [...data].sort((a, b) => (a.pointsRequired ?? 0) - (b.pointsRequired ?? 0));
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load badges.';
        this.loading = false;
      }
    });
  }

  // =========================
  // FILTERS
  // =========================
  applyFilter(): void {
    const q = this.nameQuery.trim().toLowerCase();

    let list = [...this.all];

    if (q) {
      list = list.filter(b => (b.name || '').toLowerCase().includes(q));
    }

    if (this.pointsFilter) {
      list = list.filter(b => {
        const p = b.pointsRequired ?? 0;
        switch (this.pointsFilter) {
          case '0-50': return p >= 0 && p <= 50;
          case '51-100': return p >= 51 && p <= 100;
          case '101-200': return p >= 101 && p <= 200;
          case '200+': return p >= 200;
          default: return true;
        }
      });
    }

    this.filtered = list;
  }

  resetFilter(): void {
    this.nameQuery = '';
    this.pointsFilter = '';
    this.applyFilter();
  }

  // =========================
  // NAV / FORM MODES
  // =========================
  startCreate(): void {
    // If already showing the create form, toggle it off
    if (this.showInlineForm && !this.isEdit) {
      this.showInlineForm = false;
      return;
    }
    this.isEdit = false;
    this.editingId = null;
    this.form.reset({
      name: '',
      description: null,
      pointsRequired: 0,
      imageUrl: null
    });
    this.showInlineForm = true;
  }

  onEdit(b: Badge): void {
    this.isEdit = true;
    this.editingId = b.idBadge ?? null;

    this.form.reset({
      name: b.name ?? '',
      description: b.description ?? null,
      pointsRequired: b.pointsRequired ?? 0,
      imageUrl: b.imageUrl ?? null
    });

    this.showInlineForm = true;
  }

  cancelForm(): void {
    this.showInlineForm = false;
    this.isEdit = false;
    this.editingId = null;
    this.error = null;
    this.form.reset({ name: '', description: null, pointsRequired: 0, imageUrl: null });
  }

  // =========================
  // CRUD
  // =========================
  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.error = null;

    const payload = this.form.getRawValue();

    if (!this.isEdit) {
      this.badgeService.create(payload).subscribe({
        next: (created) => {
          this.all = [created, ...this.all].sort((a, b) => (a.pointsRequired ?? 0) - (b.pointsRequired ?? 0));
          this.applyFilter();
          this.loading = false;
          this.showInlineForm = false;
          this.form.reset({ name: '', description: null, pointsRequired: 0, imageUrl: null });
        },
        error: () => {
          this.error = 'Failed to create badge.';
          this.loading = false;
        }
      });
      return;
    }

    if (!this.editingId) {
      this.error = 'Missing badge ID.';
      this.loading = false;
      return;
    }

    this.badgeService.update(this.editingId, payload).subscribe({
      next: (updated) => {
        this.all = this.all.map(x => (x.idBadge === updated.idBadge ? updated : x))
          .sort((a, b) => (a.pointsRequired ?? 0) - (b.pointsRequired ?? 0));
        this.applyFilter();
        this.loading = false;
        this.showInlineForm = false;
        this.isEdit = false;
        this.editingId = null;
        this.form.reset({ name: '', description: null, pointsRequired: 0, imageUrl: null });
      },
      error: () => {
        this.error = 'Failed to update badge.';
        this.loading = false;
      }
    });
  }

  onDelete(b: Badge): void {
    const id = b.idBadge;
    if (!id) return;

    const ok = confirm(`Delete badge "${b.name}"?`);
    if (!ok) return;

    this.loading = true;
    this.error = null;

    this.badgeService.delete(id).subscribe({
      next: () => {
        this.all = this.all.filter(x => x.idBadge !== id);
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to delete badge.';
        this.loading = false;
      }
    });
  }
}
