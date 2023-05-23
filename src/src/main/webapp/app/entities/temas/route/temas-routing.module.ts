import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TemasComponent } from '../list/temas.component';
import { TemasDetailComponent } from '../detail/temas-detail.component';
import { TemasUpdateComponent } from '../update/temas-update.component';
import { TemasRoutingResolveService } from './temas-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const temasRoute: Routes = [
  {
    path: '',
    component: TemasComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TemasDetailComponent,
    resolve: {
      temas: TemasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TemasUpdateComponent,
    resolve: {
      temas: TemasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TemasUpdateComponent,
    resolve: {
      temas: TemasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(temasRoute)],
  exports: [RouterModule],
})
export class TemasRoutingModule {}
