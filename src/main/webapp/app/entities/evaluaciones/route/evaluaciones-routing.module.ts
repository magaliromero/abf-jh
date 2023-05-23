import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EvaluacionesComponent } from '../list/evaluaciones.component';
import { EvaluacionesDetailComponent } from '../detail/evaluaciones-detail.component';
import { EvaluacionesUpdateComponent } from '../update/evaluaciones-update.component';
import { EvaluacionesRoutingResolveService } from './evaluaciones-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const evaluacionesRoute: Routes = [
  {
    path: '',
    component: EvaluacionesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EvaluacionesDetailComponent,
    resolve: {
      evaluaciones: EvaluacionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EvaluacionesUpdateComponent,
    resolve: {
      evaluaciones: EvaluacionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EvaluacionesUpdateComponent,
    resolve: {
      evaluaciones: EvaluacionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(evaluacionesRoute)],
  exports: [RouterModule],
})
export class EvaluacionesRoutingModule {}
