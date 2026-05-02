export type Role = 'STUDENT' | 'TUTOR' | 'EMPLOYE' | 'COMPANY' | 'ADMIN';

export interface User {
  email: string;
  role: Role;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: Role;
}

// Decoded JWT payload shape — Spring Security may use different claim names
export interface JwtPayload {
  sub: string;
  role?: string;
  roles?: string[];
  authorities?: string[];
  userId?: number;
  id?: number;
  exp: number;
  iat: number;
  firstName?: string;  // ✅ ajoute
  lastName?: string;   // ✅ ajoute
}