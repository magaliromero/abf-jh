import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegistroClasesComponent } from '../list/registro-clases.component';
import { RegistroClasesDetailComponent } from '../detail/registro-clases-detail.component';
import { RegistroClasesUpdateComponent } from '../update/registro-clases-update.component';
import { RegistroClasesRoutingResolveService } from './registro-clases-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const registroClasesRoute: Routes = [
  {
    path: '',
    component: RegistroClasesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegistroClasesDetailComponent,
    resolve: {
      registroClases: RegistroClasesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegistroClasesUpdateComponent,
    resolve: {
      registroClases: RegistroClasesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegistroClasesUpdateComponent,
    resolve: {
      registroClases: RegistroClasesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(registroClasesRoute)],
  exports: [RouterModule],
})
export class RegistroClasesRoutingModule {}
