import { Component, OnInit } from '@angular/core';
import { OfferService } from '../../../../core/services/language/offer.service';
import { CompanyOfferService } from '../../../../core/services/language/company-offer.service';
import { UserService } from '../../../../core/services/language/user.service';
import { Offer, EmployeeInvitation, UserDTO } from '../../../../core/models/business-english.model';
import { AuthService } from '../../../../core/services/auth.service';
import { CourseService } from '../../../../core/services/course.service';
import { Course } from '../../../../core/models/course.model';

@Component({
  selector: 'app-offers',
  templateUrl: './offers.component.html',
  styleUrls: ['./offers.component.css']
})
export class OffersComponent implements OnInit {

  offers: Offer[] = [];
  invitations: EmployeeInvitation[] = [];
  company: UserDTO | null = null;
  allCourses: Course[] = [];
  selectedOfferCourses: Course[] = [];

  loading = false;
  errorMsg = '';

  selectedOffer: Offer | null = null;
  showRequestForm = false;
  showCoursesModal = false;
  emailsText = '';

  companyId!: number;

  constructor(
    private offerService: OfferService,
    private companyOfferService: CompanyOfferService,
    private userService: UserService,
    private authService: AuthService,
    private courseService: CourseService
  ) { }

  ngOnInit(): void {
    this.companyId = this.authService.getUserId()!;
    this.loadCompany();
    this.loadActiveOffers();
    this.loadAllCourses();
  }

  loadAllCourses(): void {
    this.courseService.getBusinessCourses().subscribe({
      next: data => this.allCourses = data ?? [],
      error: err => console.error('Error loading business courses:', err)
    });
  }

  loadCompany(): void {
    this.userService.getById(this.companyId).subscribe({
      next: data => this.company = data,
      error: err => {
        console.error('Error loading company:', err);
        this.company = null;
      }
    });
  }

  loadActiveOffers(): void {
    this.loading = true;
    this.errorMsg = '';

    this.offerService.getActive().subscribe({
      next: data => {
        this.offers = data ?? [];
        this.loading = false;
      },
      error: err => {
        console.error('Error loading active offers:', err);
        this.offers = [];
        this.errorMsg = 'Cannot load offers';
        this.loading = false;
      }
    });
  }

  refresh(): void {
    this.closeRequest();
    this.loadActiveOffers();
    this.loadAllCourses();
  }

  openRequest(offer: Offer): void {
    this.selectedOffer = offer;
    this.showRequestForm = true;
    this.showCoursesModal = false;
    this.invitations = [];
    this.emailsText = '';
    this.errorMsg = '';
  }

  closeRequest(): void {
    this.showRequestForm = false;
    this.selectedOffer = null;
    this.invitations = [];
    this.emailsText = '';
    this.errorMsg = '';
  }

  openCourses(offer: Offer): void {
    this.selectedOffer = offer;
    this.selectedOfferCourses = this.allCourses.filter(c =>
      offer.courseIds?.includes(c.courseId!)
    );
    this.showCoursesModal = true;
    this.showRequestForm = false;
  }

  closeCourses(): void {
    this.showCoursesModal = false;
    this.selectedOffer = null;
    this.selectedOfferCourses = [];
  }

  private parseEmails(): string[] {
    return this.emailsText
      .split(/[\n,;]+/)
      .map(e => e.trim().toLowerCase())
      .filter(e => e.length > 0);
  }

  private isValidEmail(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  sendRequest(): void {
    if (!this.selectedOffer) {
      this.errorMsg = 'Please select an offer';
      return;
    }

    const emails = this.parseEmails();

    if (emails.length === 0) {
      this.errorMsg = 'Add at least one email';
      return;
    }

    const invalidEmails = emails.filter(e => !this.isValidEmail(e));
    if (invalidEmails.length > 0) {
      this.errorMsg = `Invalid emails: ${invalidEmails.join(', ')}`;
      return;
    }

    this.loading = true;
    this.errorMsg = '';
    this.invitations = [];

    this.companyOfferService
      .requestOffer(this.companyId, this.selectedOffer.id!, emails)
      .subscribe({
        next: (res: EmployeeInvitation[]) => {
          this.invitations = res ?? [];
          this.loading = false;
          // Auto-close modal after 2 seconds on success
          setTimeout(() => {
            if (this.invitations.length > 0) {
              this.closeRequest();
            }
          }, 2000);
        },
        error: (err) => {
          console.error('Request failed:', err);
          this.errorMsg = 'Request failed. Check backend logs.';
          this.loading = false;
        }
      });
  }
}