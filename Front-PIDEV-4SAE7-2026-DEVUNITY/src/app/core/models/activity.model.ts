export type ActivityStatus = 'PLANNED' | 'ONGOING' | 'FINISHED' | 'CANCELLED';

export interface Excursion {
  excursionId?: number;
  title: string;
  description?: string;
  location?: string;
  startDate: string;
  endDate: string;
  status: ActivityStatus;
  nbrDeplace?: number;
  nbrDeReservation?: number;
  nbrReservations?: number;
  club?: { clubId: number };
}

export interface Training {
  trainingId?: number;
  title: string;
  description?: string;
  trainer?: string;
  startDate: string;
  endDate: string;
  status: ActivityStatus;
  nbrDeplace?: number;
  nbrDeReservation?: number;
  nbrReservations?: number;
  price?: number;
  rewardEnabled?: boolean;
  rewardMax?: number;
  club?: { clubId: number };
}