import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAlumnos } from '../alumnos.model';
import { AlumnosService } from '../service/alumnos.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './alumnos-delete-dialog.component.html',
})
export class AlumnosDeleteDialogComponent {
  alumnos?: IAlumnos;

  constructor(protected alumnosService: AlumnosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alumnosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
