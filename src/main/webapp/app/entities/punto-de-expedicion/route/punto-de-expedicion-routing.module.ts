import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PuntoDeExpedicionComponent } from '../list/punto-de-expedicion.component';
import { PuntoDeExpedicionDetailComponent } from '../detail/punto-de-expedicion-detail.component';
import { PuntoDeExpedicionUpdateComponent } from '../update/punto-de-expedicion-update.component';
import { PuntoDeExpedicionRoutingResolveService } from './punto-de-expedicion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const puntoDeExpedicionRoute: Routes = [
  {
    path: '',
    component: PuntoDeExpedicionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PuntoDeExpedicionDetailComponent,
    resolve: {
      puntoDeExpedicion: PuntoDeExpedicionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PuntoDeExpedicionUpdateComponent,
    resolve: {
      puntoDeExpedicion: PuntoDeExpedicionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PuntoDeExpedicionUpdateComponent,
    resolve: {
      puntoDeExpedicion: PuntoDeExpedicionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(puntoDeExpedicionRoute)],
  exports: [RouterModule],
})
export class PuntoDeExpedicionRoutingModule {}
