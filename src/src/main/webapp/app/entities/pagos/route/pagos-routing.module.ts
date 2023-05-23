import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PagosComponent } from '../list/pagos.component';
import { PagosDetailComponent } from '../detail/pagos-detail.component';
import { PagosUpdateComponent } from '../update/pagos-update.component';
import { PagosRoutingResolveService } from './pagos-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pagosRoute: Routes = [
  {
    path: '',
    component: PagosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PagosDetailComponent,
    resolve: {
      pagos: PagosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PagosUpdateComponent,
    resolve: {
      pagos: PagosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PagosUpdateComponent,
    resolve: {
      pagos: PagosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pagosRoute)],
  exports: [RouterModule],
})
export class PagosRoutingModule {}
