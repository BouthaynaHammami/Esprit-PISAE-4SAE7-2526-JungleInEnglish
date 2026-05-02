// src/app/features/unauthorized/unauthorized.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  template: `
    <div class="flex flex-col items-center justify-center h-screen">
      <h1 class="text-5xl font-bold text-red-500">403</h1>
      <p class="text-xl text-gray-600 mt-4">You are not authorized to access this page.</p>
      <button (click)="goHome()" class="mt-6 px-6 py-3 bg-[#006D77] text-white rounded-xl">
        Go Home
      </button>
    </div>
  `
})
export class UnauthorizedComponent {
  constructor(private router: Router) {}
  goHome(): void { this.router.navigate(['/']); }
}