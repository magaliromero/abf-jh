import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEvaluaciones } from '../evaluaciones.model';
import { EvaluacionesService } from '../service/evaluaciones.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './evaluaciones-delete-dialog.component.html',
})
export class EvaluacionesDeleteDialogComponent {
  evaluaciones?: IEvaluaciones;

  constructor(protected evaluacionesService: EvaluacionesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evaluacionesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
