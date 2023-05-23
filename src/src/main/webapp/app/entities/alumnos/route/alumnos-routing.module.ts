import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AlumnosComponent } from '../list/alumnos.component';
import { AlumnosDetailComponent } from '../detail/alumnos-detail.component';
import { AlumnosUpdateComponent } from '../update/alumnos-update.component';
import { AlumnosRoutingResolveService } from './alumnos-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const alumnosRoute: Routes = [
  {
    path: '',
    component: AlumnosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AlumnosDetailComponent,
    resolve: {
      alumnos: AlumnosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AlumnosUpdateComponent,
    resolve: {
      alumnos: AlumnosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AlumnosUpdateComponent,
    resolve: {
      alumnos: AlumnosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(alumnosRoute)],
  exports: [RouterModule],
})
export class AlumnosRoutingModule {}
