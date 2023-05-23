import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MaterialesComponent } from './list/materiales.component';
import { MaterialesDetailComponent } from './detail/materiales-detail.component';
import { MaterialesUpdateComponent } from './update/materiales-update.component';
import { MaterialesDeleteDialogComponent } from './delete/materiales-delete-dialog.component';
import { MaterialesRoutingModule } from './route/materiales-routing.module';

@NgModule({
  imports: [SharedModule, MaterialesRoutingModule],
  declarations: [MaterialesComponent, MaterialesDetailComponent, MaterialesUpdateComponent, MaterialesDeleteDialogComponent],
})
export class MaterialesModule {}
