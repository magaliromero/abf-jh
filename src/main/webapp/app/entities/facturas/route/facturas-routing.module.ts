import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FacturasComponent } from '../list/facturas.component';
import { FacturasDetailComponent } from '../detail/facturas-detail.component';
import { FacturasUpdateComponent } from '../update/facturas-update.component';
import { FacturasRoutingResolveService } from './facturas-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const facturasRoute: Routes = [
  {
    path: '',
    component: FacturasComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FacturasDetailComponent,
    resolve: {
      facturas: FacturasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FacturasUpdateComponent,
    resolve: {
      facturas: FacturasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FacturasUpdateComponent,
    resolve: {
      facturas: FacturasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(facturasRoute)],
  exports: [RouterModule],
})
export class FacturasRoutingModule {}
