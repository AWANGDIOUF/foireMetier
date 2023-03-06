import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SalonMetierComponent } from '../list/salon-metier.component';
import { SalonMetierDetailComponent } from '../detail/salon-metier-detail.component';
import { SalonMetierUpdateComponent } from '../update/salon-metier-update.component';
import { SalonMetierRoutingResolveService } from './salon-metier-routing-resolve.service';

const salonMetierRoute: Routes = [
  {
    path: '',
    component: SalonMetierComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SalonMetierDetailComponent,
    resolve: {
      salonMetier: SalonMetierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SalonMetierUpdateComponent,
    resolve: {
      salonMetier: SalonMetierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SalonMetierUpdateComponent,
    resolve: {
      salonMetier: SalonMetierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(salonMetierRoute)],
  exports: [RouterModule],
})
export class SalonMetierRoutingModule {}
