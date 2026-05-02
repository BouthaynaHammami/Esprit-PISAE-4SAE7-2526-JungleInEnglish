export type ClubRole =
  | 'SPORT'
  | 'CULTURAL'
  | 'SCIENTIFIC'
  | 'TECHNOLOGICAL'
  | 'ARTISTIC'
  | 'SOCIAL';

export type ClubStatus = 'ACTIVE' | 'INACTIVE' | 'ARCHIVED';

export interface Club {
  clubId?: number;
  name: string;
  description?: string;
  creationDate?: string; // "YYYY-MM-DD"
  type: ClubRole;
  status: ClubStatus;
}