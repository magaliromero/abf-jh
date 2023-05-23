import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TiposDocumentosComponent } from './list/tipos-documentos.component';
import { TiposDocumentosDetailComponent } from './detail/tipos-documentos-detail.component';
import { TiposDocumentosUpdateComponent } from './update/tipos-documentos-update.component';
import { TiposDocumentosDeleteDialogComponent } from './delete/tipos-documentos-delete-dialog.component';
import { TiposDocumentosRoutingModule } from './route/tipos-documentos-routing.module';

@NgModule({
  imports: [SharedModule, TiposDocumentosRoutingModule],
  declarations: [
    TiposDocumentosComponent,
    TiposDocumentosDetailComponent,
    TiposDocumentosUpdateComponent,
    TiposDocumentosDeleteDialogComponent,
  ],
})
export class TiposDocumentosModule {}
