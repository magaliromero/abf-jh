import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInscripciones } from '../inscripciones.model';
import { InscripcionesService } from '../service/inscripciones.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './inscripciones-delete-dialog.component.html',
})
export class InscripcionesDeleteDialogComponent {
  inscripciones?: IInscripciones;

  constructor(protected inscripcionesService: InscripcionesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscripcionesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
