import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EvaluacionesComponent } from './list/evaluaciones.component';
import { EvaluacionesDetailComponent } from './detail/evaluaciones-detail.component';
import { EvaluacionesUpdateComponent } from './update/evaluaciones-update.component';
import { EvaluacionesDeleteDialogComponent } from './delete/evaluaciones-delete-dialog.component';
import { EvaluacionesRoutingModule } from './route/evaluaciones-routing.module';

@NgModule({
  imports: [SharedModule, EvaluacionesRoutingModule],
  declarations: [EvaluacionesComponent, EvaluacionesDetailComponent, EvaluacionesUpdateComponent, EvaluacionesDeleteDialogComponent],
})
export class EvaluacionesModule {}
