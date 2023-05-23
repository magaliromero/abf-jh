import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EvaluacionesDetalleComponent } from '../list/evaluaciones-detalle.component';
import { EvaluacionesDetalleDetailComponent } from '../detail/evaluaciones-detalle-detail.component';
import { EvaluacionesDetalleUpdateComponent } from '../update/evaluaciones-detalle-update.component';
import { EvaluacionesDetalleRoutingResolveService } from './evaluaciones-detalle-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const evaluacionesDetalleRoute: Routes = [
  {
    path: '',
    component: EvaluacionesDetalleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EvaluacionesDetalleDetailComponent,
    resolve: {
      evaluacionesDetalle: EvaluacionesDetalleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EvaluacionesDetalleUpdateComponent,
    resolve: {
      evaluacionesDetalle: EvaluacionesDetalleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EvaluacionesDetalleUpdateComponent,
    resolve: {
      evaluacionesDetalle: EvaluacionesDetalleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(evaluacionesDetalleRoute)],
  exports: [RouterModule],
})
export class EvaluacionesDetalleRoutingModule {}
