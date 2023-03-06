import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IntroductionComponent } from '../list/introduction.component';
import { IntroductionDetailComponent } from '../detail/introduction-detail.component';
import { IntroductionUpdateComponent } from '../update/introduction-update.component';
import { IntroductionRoutingResolveService } from './introduction-routing-resolve.service';

const introductionRoute: Routes = [
  {
    path: '',
    component: IntroductionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IntroductionDetailComponent,
    resolve: {
      introduction: IntroductionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IntroductionUpdateComponent,
    resolve: {
      introduction: IntroductionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IntroductionUpdateComponent,
    resolve: {
      introduction: IntroductionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(introductionRoute)],
  exports: [RouterModule],
})
export class IntroductionRoutingModule {}
