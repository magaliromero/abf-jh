import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CursosComponent } from './list/cursos.component';
import { CursosDetailComponent } from './detail/cursos-detail.component';
import { CursosUpdateComponent } from './update/cursos-update.component';
import { CursosDeleteDialogComponent } from './delete/cursos-delete-dialog.component';
import { CursosRoutingModule } from './route/cursos-routing.module';

@NgModule({
  imports: [SharedModule, CursosRoutingModule],
  declarations: [CursosComponent, CursosDetailComponent, CursosUpdateComponent, CursosDeleteDialogComponent],
})
export class CursosModule {}
