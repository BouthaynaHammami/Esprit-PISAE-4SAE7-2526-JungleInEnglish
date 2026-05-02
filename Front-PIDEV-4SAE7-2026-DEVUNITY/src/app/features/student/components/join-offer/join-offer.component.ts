import { Component, OnInit } from '@angular/core';
import { EmployeeInvitationService } from '../../../../core/services/language/employee-invitation.service';
import { AuthService } from '../../../../core/services/auth.service';
import { OfferService } from '../../../../core/services/language/offer.service';
import { EmployeeInvitation, Course, ActivationResponseDTO, Offer } from '../../../../core/models/business-english.model';
import { CourseService } from '../../../../core/services/course.service';

@Component({
  selector: 'app-student-join-offer',
  templateUrl: './join-offer.component.html',
  styleUrls: ['./join-offer.component.css']
})
export class StudentJoinOfferComponent implements OnInit {

  activationCode: string = '';
  email: string | null = null;

  loading = false;
  errorMsg = '';
  success = false;

  invitation: EmployeeInvitation | null = null;
  offerCourses: Course[] = [];

  // Enrolled offers data
  enrolledOffers: Offer[] = [];
  selectedOffer: Offer | null = null;
  loadingOffers = false;
  loadingCourses = false;

  allCourses: Course[] = [];

  constructor(
    private invitationService: EmployeeInvitationService,
    private offerService: OfferService,
    private authService: AuthService,
    private courseService: CourseService
  ) { }

  ngOnInit(): void {
    this.email = this.authService.getUserEmail();
    this.loadEnrolledOffers();
    this.loadAllCourses();
  }

  loadAllCourses(): void {
    this.courseService.getAll().subscribe({
      next: (data) => {
        this.allCourses = data ?? [];
      },
      error: (err) => {
        console.error('Failed to load courses:', err);
      }
    });
  }

  loadEnrolledOffers(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.loadingOffers = true;
    this.offerService.getOffersByStudent(userId).subscribe({
      next: (offers) => {
        this.enrolledOffers = offers;
        this.loadingOffers = false;
      },
      error: (err) => {
        console.error('Failed to load enrolled offers:', err);
        this.loadingOffers = false;
      }
    });
  }

  selectOffer(offer: Offer): void {
    if (!offer.id) return;
    
    this.selectedOffer = offer;
    this.loadingCourses = false;
    
    this.offerCourses = this.allCourses.filter(c => 
      offer.courseIds?.includes(c.courseId!)
    );

    // Scroll to courses section
    setTimeout(() => {
      document.querySelector('.courses-section')?.scrollIntoView({ behavior: 'smooth' });
    }, 100);
  }

  deselectOffer(): void {
    this.selectedOffer = null;
    this.offerCourses = [];
  }

  joinOffer(): void {

    if (!this.email || !this.activationCode.trim()) {
      this.errorMsg = 'Please enter a valid activation code.';
      return;
    }

    this.loading = true;
    this.errorMsg = '';
    this.success = false;
    this.invitation = null;
    this.offerCourses = [];

    this.invitationService.activate(
      this.email,
      this.activationCode.trim()
    ).subscribe({
      next: (res: ActivationResponseDTO) => {

        // ✅ backend response
        this.invitation = res.invitation;
        this.offerCourses = res.courses;

        this.success = true;
        this.loading = false;
        this.loadEnrolledOffers(); // Refresh the list
      },

      error: (err) => {
        console.error('Activation failed:', err);
        this.errorMsg = 'Invalid or expired activation code.';
        this.loading = false;
      }
    });
  }

  reset(): void {
    this.activationCode = '';
    this.invitation = null;
    this.offerCourses = [];
    this.success = false;
    this.errorMsg = '';
  }
}