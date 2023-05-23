import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITiposDocumentos } from '../tipos-documentos.model';
import { TiposDocumentosService } from '../service/tipos-documentos.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tipos-documentos-delete-dialog.component.html',
})
export class TiposDocumentosDeleteDialogComponent {
  tiposDocumentos?: ITiposDocumentos;

  constructor(protected tiposDocumentosService: TiposDocumentosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tiposDocumentosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
