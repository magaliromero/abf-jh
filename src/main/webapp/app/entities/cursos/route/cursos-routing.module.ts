import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CursosComponent } from '../list/cursos.component';
import { CursosDetailComponent } from '../detail/cursos-detail.component';
import { CursosUpdateComponent } from '../update/cursos-update.component';
import { CursosRoutingResolveService } from './cursos-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const cursosRoute: Routes = [
  {
    path: '',
    component: CursosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CursosDetailComponent,
    resolve: {
      cursos: CursosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CursosUpdateComponent,
    resolve: {
      cursos: CursosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CursosUpdateComponent,
    resolve: {
      cursos: CursosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cursosRoute)],
  exports: [RouterModule],
})
export class CursosRoutingModule {}
