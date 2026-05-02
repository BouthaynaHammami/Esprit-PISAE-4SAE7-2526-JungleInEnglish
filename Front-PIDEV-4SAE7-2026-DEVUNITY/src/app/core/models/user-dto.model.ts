// src/app/core/models/user-dto.model.ts

export interface UserDTO {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  classId?: number;
}
