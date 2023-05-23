import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MaterialesFormService, MaterialesFormGroup } from './materiales-form.service';
import { IMateriales } from '../materiales.model';
import { MaterialesService } from '../service/materiales.service';

@Component({
  selector: 'jhi-materiales-update',
  templateUrl: './materiales-update.component.html',
})
export class MaterialesUpdateComponent implements OnInit {
  isSaving = false;
  materiales: IMateriales | null = null;

  editForm: MaterialesFormGroup = this.materialesFormService.createMaterialesFormGroup();

  constructor(
    protected materialesService: MaterialesService,
    protected materialesFormService: MaterialesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materiales }) => {
      this.materiales = materiales;
      if (materiales) {
        this.updateForm(materiales);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const materiales = this.materialesFormService.getMateriales(this.editForm);
    if (materiales.id !== null) {
      this.subscribeToSaveResponse(this.materialesService.update(materiales));
    } else {
      this.subscribeToSaveResponse(this.materialesService.create(materiales));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMateriales>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(materiales: IMateriales): void {
    this.materiales = materiales;
    this.materialesFormService.resetForm(this.editForm, materiales);
  }
}
