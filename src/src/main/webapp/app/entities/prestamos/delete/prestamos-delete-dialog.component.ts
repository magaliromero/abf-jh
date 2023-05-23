import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrestamos } from '../prestamos.model';
import { PrestamosService } from '../service/prestamos.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './prestamos-delete-dialog.component.html',
})
export class PrestamosDeleteDialogComponent {
  prestamos?: IPrestamos;

  constructor(protected prestamosService: PrestamosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prestamosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
