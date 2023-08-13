import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SucursalesComponent } from './list/sucursales.component';
import { SucursalesDetailComponent } from './detail/sucursales-detail.component';
import { SucursalesUpdateComponent } from './update/sucursales-update.component';
import { SucursalesDeleteDialogComponent } from './delete/sucursales-delete-dialog.component';
import { SucursalesRoutingModule } from './route/sucursales-routing.module';

@NgModule({
  imports: [SharedModule, SucursalesRoutingModule],
  declarations: [SucursalesComponent, SucursalesDetailComponent, SucursalesUpdateComponent, SucursalesDeleteDialogComponent],
})
export class SucursalesModule {}
