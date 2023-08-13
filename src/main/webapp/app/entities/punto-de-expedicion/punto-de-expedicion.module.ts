import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PuntoDeExpedicionComponent } from './list/punto-de-expedicion.component';
import { PuntoDeExpedicionDetailComponent } from './detail/punto-de-expedicion-detail.component';
import { PuntoDeExpedicionUpdateComponent } from './update/punto-de-expedicion-update.component';
import { PuntoDeExpedicionDeleteDialogComponent } from './delete/punto-de-expedicion-delete-dialog.component';
import { PuntoDeExpedicionRoutingModule } from './route/punto-de-expedicion-routing.module';

@NgModule({
  imports: [SharedModule, PuntoDeExpedicionRoutingModule],
  declarations: [
    PuntoDeExpedicionComponent,
    PuntoDeExpedicionDetailComponent,
    PuntoDeExpedicionUpdateComponent,
    PuntoDeExpedicionDeleteDialogComponent,
  ],
})
export class PuntoDeExpedicionModule {}
