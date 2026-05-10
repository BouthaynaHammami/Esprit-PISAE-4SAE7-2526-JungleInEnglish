export interface AuthResponse {
  token: string;
  refreshToken?: string;
  role: string;
  email: string;
  userId: number;
  firstName?: string;
  lastName?: string;
}
