import { Component, OnInit } from '@angular/core';
import { CompanyOfferService } from '../../../../../core/services/language/company-offer.service';
import { EmployeeInvitationService } from '../../../../../core/services/language/employee-invitation.service';

@Component({
  selector: 'app-employee-invitation',
  templateUrl: './admin-employee-invitation.component.html',
  styleUrls: ['./admin-employee-invitation.component.scss']
})
export class AdminEmployeeInvitationComponent implements OnInit {
  requests: any[] = [];
  filteredRequests: any[] = [];
  searchTerm: string = '';
  loading = false;
  
  // Modal State
  showModal = false;
  selectedRequest: any = null;
  newEmail: string = '';
  isSaving = false;

  // For Payment Status dropdown
  paymentStatuses = ['PENDING', 'PAID', 'FAILED'];


  constructor(
    private companyOfferService: CompanyOfferService,
    private invitationService: EmployeeInvitationService
  ) {}

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests() {
    this.loading = true;
    this.companyOfferService.getAllRequestsForAdmin()
      .subscribe({
        next: (data: any[]) => {
          this.requests = data ?? [];
          this.applyFilter();
          this.loading = false;
        },
        error: (err: any) => {
          console.error(err);
          this.loading = false;
        }
      });
  }

  applyFilter() {
    const q = this.searchTerm.trim().toLowerCase();
    if (!q) {
      this.filteredRequests = [...this.requests];
    } else {
      this.filteredRequests = this.requests.filter(r => 
        (r.company?.firstName || '').toLowerCase().includes(q) ||
        (r.company?.lastName || '').toLowerCase().includes(q) ||
        (r.company?.email || '').toLowerCase().includes(q) ||
        (r.offerName || '').toLowerCase().includes(q)
      );
    }
  }

  // --- MODAL METHODS ---
  openModal(request: any) {
    this.selectedRequest = JSON.parse(JSON.stringify(request)); // Deep clone
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.selectedRequest = null;
    this.newEmail = '';
  }

  updatePayment(status: string) {
    if (!this.selectedRequest) return;
    this.selectedRequest.paymentStatus = status;
    
    this.companyOfferService.updatePaymentStatus(this.selectedRequest.companyOfferId, status)
      .subscribe({
        next: () => {
          // Update local list too
          const req = this.requests.find(r => r.companyOfferId === this.selectedRequest.companyOfferId);
          if (req) req.paymentStatus = status;
          alert("Payment status updated successfully.");
        },
        error: (err: any) => console.error(err)
      });
  }

  addEmail() {
    if (!this.newEmail.trim()) return;
    if (!this.selectedRequest.invitations) this.selectedRequest.invitations = [];
    
    // Check for duplicates in local UI
    if (this.selectedRequest.invitations.find((i: any) => i.email === this.newEmail.trim())) {
      alert("This email is already in the list.");
      return;
    }

    this.selectedRequest.invitations.push({
      email: this.newEmail.trim(),
      status: 'PENDING'
    });
    this.newEmail = '';
  }

  removeEmail(index: number) {
    this.selectedRequest.invitations.splice(index, 1);
  }

  saveEmails() {
    if (!this.selectedRequest) return;
    this.isSaving = true;

    // Map invitations to array of strings (emails)
    const emailList = this.selectedRequest.invitations.map((inv: any) => inv.email);

    this.invitationService.updateEmails(this.selectedRequest.companyOfferId, emailList)
      .subscribe({
        next: () => {
          // Update local list
          const original = this.requests.find(r => r.companyOfferId === this.selectedRequest.companyOfferId);
          if (original) {
            original.invitations = [...this.selectedRequest.invitations];
          }
          this.isSaving = false;
          this.closeModal();
          alert("Invitation list updated successfully on server.");
        },
        error: (err: any) => {
          console.error(err);
          this.isSaving = false;
          alert("Failed to update emails on server.");
        }
      });
  }



  approve(companyOfferId: number) {

    if(!confirm("Approve this request ?")) return;

    this.invitationService.approve(companyOfferId)
      .subscribe({
        next: () => {
          alert("Request approved and activation codes sent!");
          this.loadRequests();
        },
        error: (err: any) => console.error(err)
      });
  }

  reject(companyOfferId: number) {

    if(!confirm("Reject this request ?")) return;

    this.invitationService.reject(companyOfferId)
      .subscribe({
        next: () => {
          alert("Request rejected.");
          this.loadRequests();
        },
        error: (err: any) => console.error(err)
      });
  }
}