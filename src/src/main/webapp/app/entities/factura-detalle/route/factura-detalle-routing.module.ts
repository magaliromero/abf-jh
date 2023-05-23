import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FacturaDetalleComponent } from '../list/factura-detalle.component';
import { FacturaDetalleDetailComponent } from '../detail/factura-detalle-detail.component';
import { FacturaDetalleUpdateComponent } from '../update/factura-detalle-update.component';
import { FacturaDetalleRoutingResolveService } from './factura-detalle-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const facturaDetalleRoute: Routes = [
  {
    path: '',
    component: FacturaDetalleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FacturaDetalleDetailComponent,
    resolve: {
      facturaDetalle: FacturaDetalleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FacturaDetalleUpdateComponent,
    resolve: {
      facturaDetalle: FacturaDetalleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FacturaDetalleUpdateComponent,
    resolve: {
      facturaDetalle: FacturaDetalleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(facturaDetalleRoute)],
  exports: [RouterModule],
})
export class FacturaDetalleRoutingModule {}
