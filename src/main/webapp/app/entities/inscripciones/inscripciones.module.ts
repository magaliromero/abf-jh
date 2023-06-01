import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InscripcionesComponent } from './list/inscripciones.component';
import { InscripcionesDetailComponent } from './detail/inscripciones-detail.component';
import { InscripcionesUpdateComponent } from './update/inscripciones-update.component';
import { InscripcionesDeleteDialogComponent } from './delete/inscripciones-delete-dialog.component';
import { InscripcionesRoutingModule } from './route/inscripciones-routing.module';

@NgModule({
  imports: [SharedModule, InscripcionesRoutingModule],
  declarations: [InscripcionesComponent, InscripcionesDetailComponent, InscripcionesUpdateComponent, InscripcionesDeleteDialogComponent],
})
export class InscripcionesModule {}
