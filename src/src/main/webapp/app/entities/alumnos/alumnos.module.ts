import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AlumnosComponent } from './list/alumnos.component';
import { AlumnosDetailComponent } from './detail/alumnos-detail.component';
import { AlumnosUpdateComponent } from './update/alumnos-update.component';
import { AlumnosDeleteDialogComponent } from './delete/alumnos-delete-dialog.component';
import { AlumnosRoutingModule } from './route/alumnos-routing.module';

@NgModule({
  imports: [SharedModule, AlumnosRoutingModule],
  declarations: [AlumnosComponent, AlumnosDetailComponent, AlumnosUpdateComponent, AlumnosDeleteDialogComponent],
})
export class AlumnosModule {}
