import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InscripcionesComponent } from '../list/inscripciones.component';
import { InscripcionesDetailComponent } from '../detail/inscripciones-detail.component';
import { InscripcionesUpdateComponent } from '../update/inscripciones-update.component';
import { InscripcionesRoutingResolveService } from './inscripciones-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const inscripcionesRoute: Routes = [
  {
    path: '',
    component: InscripcionesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InscripcionesDetailComponent,
    resolve: {
      inscripciones: InscripcionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InscripcionesUpdateComponent,
    resolve: {
      inscripciones: InscripcionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InscripcionesUpdateComponent,
    resolve: {
      inscripciones: InscripcionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inscripcionesRoute)],
  exports: [RouterModule],
})
export class InscripcionesRoutingModule {}
