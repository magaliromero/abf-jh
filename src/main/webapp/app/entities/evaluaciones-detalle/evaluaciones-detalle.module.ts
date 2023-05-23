import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EvaluacionesDetalleComponent } from './list/evaluaciones-detalle.component';
import { EvaluacionesDetalleDetailComponent } from './detail/evaluaciones-detalle-detail.component';
import { EvaluacionesDetalleUpdateComponent } from './update/evaluaciones-detalle-update.component';
import { EvaluacionesDetalleDeleteDialogComponent } from './delete/evaluaciones-detalle-delete-dialog.component';
import { EvaluacionesDetalleRoutingModule } from './route/evaluaciones-detalle-routing.module';

@NgModule({
  imports: [SharedModule, EvaluacionesDetalleRoutingModule],
  declarations: [
    EvaluacionesDetalleComponent,
    EvaluacionesDetalleDetailComponent,
    EvaluacionesDetalleUpdateComponent,
    EvaluacionesDetalleDeleteDialogComponent,
  ],
})
export class EvaluacionesDetalleModule {}
