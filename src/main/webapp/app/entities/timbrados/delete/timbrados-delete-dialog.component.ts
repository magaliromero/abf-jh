import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITimbrados } from '../timbrados.model';
import { TimbradosService } from '../service/timbrados.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './timbrados-delete-dialog.component.html',
})
export class TimbradosDeleteDialogComponent {
  timbrados?: ITimbrados;

  constructor(protected timbradosService: TimbradosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.timbradosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
