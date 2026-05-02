/* =========================
   ENUMS
========================= */

import { Course } from "./course.model";
export type { Course };

export enum ApprovalStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}

export enum PaymentStatus {
  PENDING = 'PENDING',
  PAID = 'PAID',
  FAILED = 'FAILED'
}

export enum InvitationStatus {
  PENDING = 'PENDING',
  SENT = 'SENT',
  USED = 'USED',
  EXPIRED = 'EXPIRED',
  REJECTED = 'REJECTED'
}

export enum LearningStatus {
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED'
}

export enum Level {
  A1 = 'A1',
  A2 = 'A2',
  B1 = 'B1',
  B2 = 'B2',
  C1 = 'C1',
  C2 = 'C2'
}

export enum Status {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE'
}

export enum RoleDTO {
  ADMIN = 'ADMIN',
  STUDENT = 'STUDENT',
  TUTOR = 'TUTOR',
  EMPLOYE = 'EMPLOYE',
  COMPANY = 'COMPANY'
}


/* =========================
   USER DTO
========================= */

export interface UserDTO {
  userId?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  role?: RoleDTO;
}


/* =========================
   OFFER
========================= */

export interface Offer {
  id?: number;
  name?: string;
  description?: string;
  startDate?: string;
  endDate?: string;
  price?: number;
  category?: string;
  durationHours?: number;

  level?: Level;
  status?: Status;

  businessEnglishPaths?: BusinessEnglishPath[];
  employeeInvitations?: EmployeeInvitation[];
  companyOffers?: CompanyOffer[];
  courseIds?: number[];
}


/* =========================
   BUSINESS ENGLISH PATH
========================= */

export interface BusinessEnglishPath {
  id?: number;
  creationDate?: string;
  expectedEndDate?: string;
  overallScore?: number;
  lastUpdateDate?: string;

  status?: LearningStatus;

  offer?: Offer;
}


/* =========================
   COMPANY OFFER
========================= */

export interface CompanyOffer {
  idCompanyOffer?: number;
  // backward-compat snake_case id used in templates/backend
  id_company_offer?: number;

  companyId?: number;
  // sometimes responses include full company object
  company?: UserDTO;

  paidDate?: string;
  paymentStatus?: PaymentStatus;
  approvalStatus?: ApprovalStatus;

  requestedAt?: string;
  decidedAt?: string;

  usersIds?: number[];

  offer?: Offer;
}


/* =========================
   EMPLOYEE INVITATION
========================= */

export interface EmployeeInvitation {
  id?: number;
  email?: string;
  activationCode?: string;

  sentDate?: string;
  expirationDate?: string;

  status?: InvitationStatus;

  companyOffer?: CompanyOffer;
  offer?: Offer;

  usersIds?: number[];
}


/* =========================
   DTOs
========================= */

export interface EmployeeInvitationDTO {
  id?: number;
  email?: string;
  status?: InvitationStatus;
  sentDate?: string;
  expirationDate?: string;
  activationCode?: string;
}

export interface CompanyOfferRequestDTO {
  companyOfferId?: number;

  companyId?: number;
  company?: UserDTO;

  offerId?: number;
  offerName?: string;

  status?: ApprovalStatus;
  paymentStatus?: PaymentStatus;

  requestedAt?: string;
  decidedAt?: string;
  paidDate?: string;

  students?: UserDTO[];
  invitations?: EmployeeInvitationDTO[];
}

export interface ActivationResponseDTO {
  invitation: EmployeeInvitation;
  courses: Course[];
}