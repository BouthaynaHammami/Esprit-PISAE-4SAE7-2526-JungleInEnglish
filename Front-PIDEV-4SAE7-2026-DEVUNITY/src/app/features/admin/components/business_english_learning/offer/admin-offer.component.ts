import { Component, OnInit } from '@angular/core';
import { OfferService } from '../../../../../core/services/language/offer.service';
import { Offer, Level, Status } from '../../../../../core/models/business-english.model';
import { HttpErrorResponse } from '@angular/common/http';
import { CourseService } from '../../../../../core/services/course.service';
import { Course } from '../../../../../core/models/course.model';


@Component({
  selector: 'app-offer',
  templateUrl: './admin-offer.component.html',
  styleUrls: ['./admin-offer.component.scss']
})
export class AdminOfferComponent implements OnInit {

  // UI state
  showInlineForm = false;
  loading = false;
  error: string | null = null;
  message: string | null = null; // Success notifications

  // Data
  allOffers: Offer[] = [];
  filtered: Offer[] = [];
  availableCourses: Course[] = [];


  // Filters
  searchTerm = '';

  // Form state
  isEdit = false;
  editingId: number | null = null;
  offer: Offer = this.emptyOffer();

  levels: Level[] = [Level.A1, Level.A2, Level.B1, Level.B2, Level.C1, Level.C2];
  statuses: Status[] = [Status.ACTIVE, Status.INACTIVE];

  constructor(
    private offerService: OfferService,
    private courseService: CourseService
  ) {}


  ngOnInit(): void {
    this.loadOffers();
    this.loadCourses();
  }


  // =========================
  // LOAD & FILTER
  // =========================
  loadOffers(): void {
    this.loading = true;
    this.error = null;
    this.offerService.getAll().subscribe({
      next: (data: Offer[]) => {
        this.allOffers = data ?? [];
        this.applyFilter();
        this.loading = false;
      },
      error: (err: HttpErrorResponse) => {
        console.error(err);
        this.error = `Impossible de charger les offres (HTTP ${err.status}).`;
        this.loading = false;
      }
    });
  }

  loadCourses(): void {
    this.courseService.getBusinessCourses().subscribe({
      next: (data: Course[]) => {
        this.availableCourses = data ?? [];
      },
      error: (err: HttpErrorResponse) => console.error('Error fetching business courses', err)
    });
  }

  applyFilter(): void {
    const q = this.searchTerm.trim().toLowerCase();
    if (!q) {
      this.filtered = [...this.allOffers];
    } else {
      this.filtered = this.allOffers.filter(o => 
        (o.name || '').toLowerCase().includes(q) || 
        (o.description || '').toLowerCase().includes(q)
      );
    }
  }

  // =========================
  // FORM ACTIONS
  // =========================
  startCreate(): void {
    if (this.showInlineForm && !this.isEdit) {
      this.showInlineForm = false;
      return;
    }
    this.isEdit = false;
    this.editingId = null;
    this.offer = this.emptyOffer();
    this.showInlineForm = true;
    this.error = null;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  onEdit(o: Offer): void {
    this.isEdit = true;
    this.editingId = o.id ?? null;
    this.offer = { ...o };
    this.showInlineForm = true;
    this.error = null;
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelForm(): void {
    this.showInlineForm = false;
    this.isEdit = false;
    this.editingId = null;
    this.offer = this.emptyOffer();
    this.error = null;
  }

  // --- COURSE SELECTION ---
  isCourseSelected(courseId: number): boolean {
    return !!this.offer.courseIds?.includes(courseId);
  }

  toggleCourse(courseId: number): void {
    if (!this.offer.courseIds) {
      this.offer.courseIds = [];
    }
    const idx = this.offer.courseIds.indexOf(courseId);
    if (idx > -1) {
      this.offer.courseIds.splice(idx, 1);
    } else {
      this.offer.courseIds.push(courseId);
    }
  }


  // =========================
  // CRUD
  // =========================
  save(): void {
    if (!this.offer.name || !this.offer.description) {
      this.error = 'Name and description are required.';
      return;
    }

    this.loading = true;
    this.error = null;

    if (this.isEdit && this.editingId) {
      this.offerService.update(this.editingId, this.offer).subscribe({
        next: () => {
          this.handleSuccess('Offre modifiée avec succès.');
        },
        error: (err: HttpErrorResponse) => {
          console.error(err);
          this.error = `Erreur modification (HTTP ${err.status}).`;
          this.loading = false;
        }
      });
    } else {
      this.offerService.create(this.offer).subscribe({
        next: () => {
          this.handleSuccess('Offre créée avec succès.');
        },
        error: (err: HttpErrorResponse) => {
          console.error(err);
          this.error = `Erreur création (HTTP ${err.status}).`;
          this.loading = false;
        }
      });
    }
  }

  private handleSuccess(msg: string): void {
    this.message = msg;
    this.showInlineForm = false;
    this.loadOffers();
    setTimeout(() => this.message = null, 3500);
  }

  deleteOffer(o: Offer): void {
    if (!o.id) return;
    const ok = confirm(`Supprimer l'offre "${o.name}" ?`);
    if (!ok) return;

    this.loading = true;
    this.offerService.delete(o.id).subscribe({
      next: () => {
        this.message = 'Offre supprimée.';
        this.loadOffers();
        setTimeout(() => this.message = null, 3000);
      },
      error: (err: HttpErrorResponse) => {
        console.error(err);
        this.error = `Erreur suppression (HTTP ${err.status}).`;
        this.loading = false;
      }
    });
  }

  private emptyOffer(): Offer {
    return {
      name: '',
      description: '',
      startDate: '',
      endDate: '',
      price: 0,
      category: '',
      durationHours: 0,
      level: Level.A1,
      status: Status.ACTIVE,
      courseIds: []
    };
  }

}

