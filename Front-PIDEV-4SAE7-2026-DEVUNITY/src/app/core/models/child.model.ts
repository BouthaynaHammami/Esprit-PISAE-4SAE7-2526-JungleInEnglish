export interface Child {
  childId?: number;
  name: string;
  birthDate: Date;
  userId?: number;  // ID du user associé
  parent?: any;
  level?: any;
  progressList?: any[];
}
