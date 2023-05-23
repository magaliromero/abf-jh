import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TemasComponent } from './list/temas.component';
import { TemasDetailComponent } from './detail/temas-detail.component';
import { TemasUpdateComponent } from './update/temas-update.component';
import { TemasDeleteDialogComponent } from './delete/temas-delete-dialog.component';
import { TemasRoutingModule } from './route/temas-routing.module';

@NgModule({
  imports: [SharedModule, TemasRoutingModule],
  declarations: [TemasComponent, TemasDetailComponent, TemasUpdateComponent, TemasDeleteDialogComponent],
})
export class TemasModule {}
