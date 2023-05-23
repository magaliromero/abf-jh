import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegistroClasesComponent } from './list/registro-clases.component';
import { RegistroClasesDetailComponent } from './detail/registro-clases-detail.component';
import { RegistroClasesUpdateComponent } from './update/registro-clases-update.component';
import { RegistroClasesDeleteDialogComponent } from './delete/registro-clases-delete-dialog.component';
import { RegistroClasesRoutingModule } from './route/registro-clases-routing.module';

@NgModule({
  imports: [SharedModule, RegistroClasesRoutingModule],
  declarations: [
    RegistroClasesComponent,
    RegistroClasesDetailComponent,
    RegistroClasesUpdateComponent,
    RegistroClasesDeleteDialogComponent,
  ],
})
export class RegistroClasesModule {}
