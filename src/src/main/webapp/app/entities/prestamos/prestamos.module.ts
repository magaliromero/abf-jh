import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrestamosComponent } from './list/prestamos.component';
import { PrestamosDetailComponent } from './detail/prestamos-detail.component';
import { PrestamosUpdateComponent } from './update/prestamos-update.component';
import { PrestamosDeleteDialogComponent } from './delete/prestamos-delete-dialog.component';
import { PrestamosRoutingModule } from './route/prestamos-routing.module';

@NgModule({
  imports: [SharedModule, PrestamosRoutingModule],
  declarations: [PrestamosComponent, PrestamosDetailComponent, PrestamosUpdateComponent, PrestamosDeleteDialogComponent],
})
export class PrestamosModule {}
