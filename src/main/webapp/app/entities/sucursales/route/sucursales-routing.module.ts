import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SucursalesComponent } from '../list/sucursales.component';
import { SucursalesDetailComponent } from '../detail/sucursales-detail.component';
import { SucursalesUpdateComponent } from '../update/sucursales-update.component';
import { SucursalesRoutingResolveService } from './sucursales-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sucursalesRoute: Routes = [
  {
    path: '',
    component: SucursalesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SucursalesDetailComponent,
    resolve: {
      sucursales: SucursalesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SucursalesUpdateComponent,
    resolve: {
      sucursales: SucursalesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SucursalesUpdateComponent,
    resolve: {
      sucursales: SucursalesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sucursalesRoute)],
  exports: [RouterModule],
})
export class SucursalesRoutingModule {}
