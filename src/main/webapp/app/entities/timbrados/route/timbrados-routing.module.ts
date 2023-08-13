import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TimbradosComponent } from '../list/timbrados.component';
import { TimbradosDetailComponent } from '../detail/timbrados-detail.component';
import { TimbradosUpdateComponent } from '../update/timbrados-update.component';
import { TimbradosRoutingResolveService } from './timbrados-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const timbradosRoute: Routes = [
  {
    path: '',
    component: TimbradosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TimbradosDetailComponent,
    resolve: {
      timbrados: TimbradosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TimbradosUpdateComponent,
    resolve: {
      timbrados: TimbradosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TimbradosUpdateComponent,
    resolve: {
      timbrados: TimbradosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(timbradosRoute)],
  exports: [RouterModule],
})
export class TimbradosRoutingModule {}
