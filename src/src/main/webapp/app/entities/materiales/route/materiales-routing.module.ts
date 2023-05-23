import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MaterialesComponent } from '../list/materiales.component';
import { MaterialesDetailComponent } from '../detail/materiales-detail.component';
import { MaterialesUpdateComponent } from '../update/materiales-update.component';
import { MaterialesRoutingResolveService } from './materiales-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const materialesRoute: Routes = [
  {
    path: '',
    component: MaterialesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaterialesDetailComponent,
    resolve: {
      materiales: MaterialesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaterialesUpdateComponent,
    resolve: {
      materiales: MaterialesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaterialesUpdateComponent,
    resolve: {
      materiales: MaterialesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(materialesRoute)],
  exports: [RouterModule],
})
export class MaterialesRoutingModule {}
