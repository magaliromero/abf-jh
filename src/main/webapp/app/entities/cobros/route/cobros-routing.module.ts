import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CobrosComponent } from '../list/cobros.component';
import { CobrosDetailComponent } from '../detail/cobros-detail.component';
import { CobrosUpdateComponent } from '../update/cobros-update.component';
import { CobrosRoutingResolveService } from './cobros-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cobrosRoute: Routes = [
  {
    path: '',
    component: CobrosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CobrosDetailComponent,
    resolve: {
      cobros: CobrosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CobrosUpdateComponent,
    resolve: {
      cobros: CobrosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CobrosUpdateComponent,
    resolve: {
      cobros: CobrosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cobrosRoute)],
  exports: [RouterModule],
})
export class CobrosRoutingModule {}
