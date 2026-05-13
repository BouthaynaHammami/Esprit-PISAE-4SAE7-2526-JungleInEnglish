// src/app/core/models/recruitment.model.ts

export type RecruitmentStatus = 'OPEN' | 'CLOSED' | 'CANCELLED';
export type ApplicantStatus   = 'PENDING' | 'ACCEPTED' | 'REJECTED';
export type MeetingStatus     = 'SCHEDULED' | 'DONE' | 'CANCELLED' | 'ENLIGNE' | 'PRESENTIEL';

export type ContractType = 'CDI' | 'CDD' | 'FREELANCE' | 'STAGE';

export interface Recruitment {
  id?: number;
  positionTitle: string;
  department: string;
  requiredSkills?: string;
  experienceYears?: number;
  description?: string;
  location?: string;
  contractType?: ContractType;
  status?: RecruitmentStatus;
  openedAt?: string | Date;
  interviews?: Interview[];
  applicants?: Applicant[];
}

export interface Interview {
  id?: number;
  title: string;
  startDateTime?: string | Date;
  durationMinutes?: number;
  meetingLink?: string;
  userId?: number;
  meetingStatus?: MeetingStatus;
  recruitment?: Partial<Recruitment>;
}

export interface Applicant {
  id?: number;
  date?: string | Date;
  reponse?: string;
  userId?: number;
  firstName?: string;
  lastName?: string;
  cv?: string;
  aiResult?: {
    cluster?: string;
    decision?: string;
    raison?: string;
    score?: number;
  };
  status?: ApplicantStatus;
  recruitment?: Partial<Recruitment>;
  interview?: Partial<Interview>;
}

export interface UserDTO {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
}

export interface CVAnalysisResult {
  applicantId: number;
  decision: 'ACCEPTED' | 'REJECTED' | 'PENDING';
  score?: number;
  summary?: string;
  strengths?: string[];
  weaknesses?: string[];
  recommendation?: string;
  analyzedAt?: string | Date;
}
