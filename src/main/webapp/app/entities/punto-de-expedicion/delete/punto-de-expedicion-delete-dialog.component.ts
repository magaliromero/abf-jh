import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPuntoDeExpedicion } from '../punto-de-expedicion.model';
import { PuntoDeExpedicionService } from '../service/punto-de-expedicion.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './punto-de-expedicion-delete-dialog.component.html',
})
export class PuntoDeExpedicionDeleteDialogComponent {
  puntoDeExpedicion?: IPuntoDeExpedicion;

  constructor(protected puntoDeExpedicionService: PuntoDeExpedicionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.puntoDeExpedicionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
