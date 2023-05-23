import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PagosComponent } from './list/pagos.component';
import { PagosDetailComponent } from './detail/pagos-detail.component';
import { PagosUpdateComponent } from './update/pagos-update.component';
import { PagosDeleteDialogComponent } from './delete/pagos-delete-dialog.component';
import { PagosRoutingModule } from './route/pagos-routing.module';

@NgModule({
  imports: [SharedModule, PagosRoutingModule],
  declarations: [PagosComponent, PagosDetailComponent, PagosUpdateComponent, PagosDeleteDialogComponent],
})
export class PagosModule {}
