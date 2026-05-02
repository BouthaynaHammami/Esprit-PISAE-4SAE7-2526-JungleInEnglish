// src/app/features/company/company.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CompanyRoutingModule } from './company-routing.module';
import { CompanyShellComponent } from './components/company-shell/company-shell.component';
import { CompanyNavbarComponent } from './components/company-navbar/company-navbar.component';
import { CompanyDashboardComponent } from './components/company-dashboard/company-dashboard.component';
import { CompanyProfileComponent } from './components/profile/company-profile.component';
import { CompanyProgressComponent } from './components/progress/company-progress.component';
import { CompanyBuyCoursesComponent } from './components/buy-courses/company-buy-courses.component';
import { OffersComponent } from './components/offers/offers.component';

@NgModule({
  declarations: [
    CompanyShellComponent,
    CompanyNavbarComponent,
    CompanyDashboardComponent,
    CompanyProfileComponent,
    CompanyProgressComponent,
    CompanyBuyCoursesComponent,
    OffersComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    CompanyRoutingModule,
  ]
})
export class CompanyModule { }