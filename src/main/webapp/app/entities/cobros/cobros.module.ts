import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CobrosComponent } from './list/cobros.component';
import { CobrosDetailComponent } from './detail/cobros-detail.component';
import { CobrosUpdateComponent } from './update/cobros-update.component';
import { CobrosDeleteDialogComponent } from './delete/cobros-delete-dialog.component';
import { CobrosRoutingModule } from './route/cobros-routing.module';

@NgModule({
  imports: [SharedModule, CobrosRoutingModule],
  declarations: [CobrosComponent, CobrosDetailComponent, CobrosUpdateComponent, CobrosDeleteDialogComponent],
})
export class CobrosModule {}
