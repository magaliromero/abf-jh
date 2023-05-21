import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FacturaDetalleComponent } from './list/factura-detalle.component';
import { FacturaDetalleDetailComponent } from './detail/factura-detalle-detail.component';
import { FacturaDetalleUpdateComponent } from './update/factura-detalle-update.component';
import { FacturaDetalleDeleteDialogComponent } from './delete/factura-detalle-delete-dialog.component';
import { FacturaDetalleRoutingModule } from './route/factura-detalle-routing.module';

@NgModule({
  imports: [SharedModule, FacturaDetalleRoutingModule],
  declarations: [
    FacturaDetalleComponent,
    FacturaDetalleDetailComponent,
    FacturaDetalleUpdateComponent,
    FacturaDetalleDeleteDialogComponent,
  ],
})
export class FacturaDetalleModule {}
