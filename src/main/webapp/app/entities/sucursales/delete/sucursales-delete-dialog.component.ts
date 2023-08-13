import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISucursales } from '../sucursales.model';
import { SucursalesService } from '../service/sucursales.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './sucursales-delete-dialog.component.html',
})
export class SucursalesDeleteDialogComponent {
  sucursales?: ISucursales;

  constructor(protected sucursalesService: SucursalesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sucursalesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
