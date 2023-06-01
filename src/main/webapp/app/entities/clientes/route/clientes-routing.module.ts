import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClientesComponent } from '../list/clientes.component';
import { ClientesDetailComponent } from '../detail/clientes-detail.component';
import { ClientesUpdateComponent } from '../update/clientes-update.component';
import { ClientesRoutingResolveService } from './clientes-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const clientesRoute: Routes = [
  {
    path: '',
    component: ClientesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClientesDetailComponent,
    resolve: {
      clientes: ClientesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClientesUpdateComponent,
    resolve: {
      clientes: ClientesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClientesUpdateComponent,
    resolve: {
      clientes: ClientesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(clientesRoute)],
  exports: [RouterModule],
})
export class ClientesRoutingModule {}
