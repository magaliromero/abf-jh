import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FuncionariosComponent } from '../list/funcionarios.component';
import { FuncionariosDetailComponent } from '../detail/funcionarios-detail.component';
import { FuncionariosUpdateComponent } from '../update/funcionarios-update.component';
import { FuncionariosRoutingResolveService } from './funcionarios-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const funcionariosRoute: Routes = [
  {
    path: '',
    component: FuncionariosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FuncionariosDetailComponent,
    resolve: {
      funcionarios: FuncionariosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FuncionariosUpdateComponent,
    resolve: {
      funcionarios: FuncionariosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FuncionariosUpdateComponent,
    resolve: {
      funcionarios: FuncionariosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(funcionariosRoute)],
  exports: [RouterModule],
})
export class FuncionariosRoutingModule {}
