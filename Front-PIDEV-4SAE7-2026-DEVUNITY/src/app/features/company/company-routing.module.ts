// src/app/features/company/company-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyShellComponent } from './components/company-shell/company-shell.component';
import { CompanyDashboardComponent } from './components/company-dashboard/company-dashboard.component';
import { CompanyProfileComponent } from './components/profile/company-profile.component';
import { CompanyProgressComponent } from './components/progress/company-progress.component';
import { CompanyBuyCoursesComponent } from './components/buy-courses/company-buy-courses.component';
import { OffersComponent } from './components/offers/offers.component';

const routes: Routes = [
    {
        path: '',
        component: CompanyShellComponent,
        children: [
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
            { path: 'dashboard', component: CompanyDashboardComponent },
            { path: 'progress', component: CompanyProgressComponent },
            { path: 'buy-courses', component: CompanyBuyCoursesComponent },
            { path: 'offers', component: OffersComponent },
            { path: 'profile', component: CompanyProfileComponent },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CompanyRoutingModule { }
