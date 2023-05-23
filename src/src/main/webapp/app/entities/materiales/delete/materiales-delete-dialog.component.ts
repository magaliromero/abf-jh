import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMateriales } from '../materiales.model';
import { MaterialesService } from '../service/materiales.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './materiales-delete-dialog.component.html',
})
export class MaterialesDeleteDialogComponent {
  materiales?: IMateriales;

  constructor(protected materialesService: MaterialesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.materialesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
