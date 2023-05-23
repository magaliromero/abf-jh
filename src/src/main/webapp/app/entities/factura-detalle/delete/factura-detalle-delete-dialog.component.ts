import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacturaDetalle } from '../factura-detalle.model';
import { FacturaDetalleService } from '../service/factura-detalle.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './factura-detalle-delete-dialog.component.html',
})
export class FacturaDetalleDeleteDialogComponent {
  facturaDetalle?: IFacturaDetalle;

  constructor(protected facturaDetalleService: FacturaDetalleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.facturaDetalleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
