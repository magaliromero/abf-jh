import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFuncionarios } from '../funcionarios.model';
import { FuncionariosService } from '../service/funcionarios.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './funcionarios-delete-dialog.component.html',
})
export class FuncionariosDeleteDialogComponent {
  funcionarios?: IFuncionarios;

  constructor(protected funcionariosService: FuncionariosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.funcionariosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
