import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacturas } from '../facturas.model';
import { FacturasService } from '../service/facturas.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './facturas-delete-dialog.component.html',
})
export class FacturasDeleteDialogComponent {
  facturas?: IFacturas;

  constructor(protected facturasService: FacturasService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facturasService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
