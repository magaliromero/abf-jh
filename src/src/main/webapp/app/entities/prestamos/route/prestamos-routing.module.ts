import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrestamosComponent } from '../list/prestamos.component';
import { PrestamosDetailComponent } from '../detail/prestamos-detail.component';
import { PrestamosUpdateComponent } from '../update/prestamos-update.component';
import { PrestamosRoutingResolveService } from './prestamos-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const prestamosRoute: Routes = [
  {
    path: '',
    component: PrestamosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrestamosDetailComponent,
    resolve: {
      prestamos: PrestamosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrestamosUpdateComponent,
    resolve: {
      prestamos: PrestamosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrestamosUpdateComponent,
    resolve: {
      prestamos: PrestamosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prestamosRoute)],
  exports: [RouterModule],
})
export class PrestamosRoutingModule {}
