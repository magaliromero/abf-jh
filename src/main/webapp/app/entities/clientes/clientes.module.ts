import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClientesComponent } from './list/clientes.component';
import { ClientesDetailComponent } from './detail/clientes-detail.component';
import { ClientesUpdateComponent } from './update/clientes-update.component';
import { ClientesDeleteDialogComponent } from './delete/clientes-delete-dialog.component';
import { ClientesRoutingModule } from './route/clientes-routing.module';

@NgModule({
  imports: [SharedModule, ClientesRoutingModule],
  declarations: [ClientesComponent, ClientesDetailComponent, ClientesUpdateComponent, ClientesDeleteDialogComponent],
})
export class ClientesModule {}
