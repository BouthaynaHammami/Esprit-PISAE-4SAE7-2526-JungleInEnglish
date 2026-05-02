export type ActivityStatus = 'PLANNED' | 'ONGOING' | 'FINISHED' | 'CANCELLED';

export interface Excursion {
  excursionId?: number;
  title: string;
  description?: string;
  location?: string;
  startDate?: string;
  endDate?: string;

  nbrDeplace?: number;        
  nbrReservations?: number;   

  status: ActivityStatus;
  club: { clubId: number };
}

export interface Training {
  trainingId?: number;
  title: string;
  description?: string;
  trainer?: string;
  startDate?: string;
  endDate?: string;

  nbrDeplace?: number;       
  nbrReservations?: number;   

  status: ActivityStatus;
  club: { clubId: number };
}