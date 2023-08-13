import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TimbradosComponent } from './list/timbrados.component';
import { TimbradosDetailComponent } from './detail/timbrados-detail.component';
import { TimbradosUpdateComponent } from './update/timbrados-update.component';
import { TimbradosDeleteDialogComponent } from './delete/timbrados-delete-dialog.component';
import { TimbradosRoutingModule } from './route/timbrados-routing.module';

@NgModule({
  imports: [SharedModule, TimbradosRoutingModule],
  declarations: [TimbradosComponent, TimbradosDetailComponent, TimbradosUpdateComponent, TimbradosDeleteDialogComponent],
})
export class TimbradosModule {}
