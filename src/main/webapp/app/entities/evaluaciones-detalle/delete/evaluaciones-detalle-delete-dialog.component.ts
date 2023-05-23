import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEvaluacionesDetalle } from '../evaluaciones-detalle.model';
import { EvaluacionesDetalleService } from '../service/evaluaciones-detalle.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './evaluaciones-detalle-delete-dialog.component.html',
})
export class EvaluacionesDetalleDeleteDialogComponent {
  evaluacionesDetalle?: IEvaluacionesDetalle;

  constructor(protected evaluacionesDetalleService: EvaluacionesDetalleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evaluacionesDetalleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
