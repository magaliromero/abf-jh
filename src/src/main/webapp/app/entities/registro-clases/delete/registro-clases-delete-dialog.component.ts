import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegistroClases } from '../registro-clases.model';
import { RegistroClasesService } from '../service/registro-clases.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './registro-clases-delete-dialog.component.html',
})
export class RegistroClasesDeleteDialogComponent {
  registroClases?: IRegistroClases;

  constructor(protected registroClasesService: RegistroClasesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.registroClasesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
