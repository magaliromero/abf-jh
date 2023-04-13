import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PrestamosFormService, PrestamosFormGroup } from './prestamos-form.service';
import { IPrestamos } from '../prestamos.model';
import { PrestamosService } from '../service/prestamos.service';
import { IMateriales } from 'app/entities/materiales/materiales.model';
import { MaterialesService } from 'app/entities/materiales/service/materiales.service';

@Component({
  selector: 'jhi-prestamos-update',
  templateUrl: './prestamos-update.component.html',
})
export class PrestamosUpdateComponent implements OnInit {
  isSaving = false;
  prestamos: IPrestamos | null = null;

  materialesSharedCollection: IMateriales[] = [];

  editForm: PrestamosFormGroup = this.prestamosFormService.createPrestamosFormGroup();

  constructor(
    protected prestamosService: PrestamosService,
    protected prestamosFormService: PrestamosFormService,
    protected materialesService: MaterialesService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMateriales = (o1: IMateriales | null, o2: IMateriales | null): boolean => this.materialesService.compareMateriales(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestamos }) => {
      this.prestamos = prestamos;
      if (prestamos) {
        this.updateForm(prestamos);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prestamos = this.prestamosFormService.getPrestamos(this.editForm);
    if (prestamos.id !== null) {
      this.subscribeToSaveResponse(this.prestamosService.update(prestamos));
    } else {
      this.subscribeToSaveResponse(this.prestamosService.create(prestamos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrestamos>>): void {
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

  protected updateForm(prestamos: IPrestamos): void {
    this.prestamos = prestamos;
    this.prestamosFormService.resetForm(this.editForm, prestamos);

    this.materialesSharedCollection = this.materialesService.addMaterialesToCollectionIfMissing<IMateriales>(
      this.materialesSharedCollection,
      prestamos.materiales
    );
  }

  protected loadRelationshipsOptions(): void {
    this.materialesService
      .query()
      .pipe(map((res: HttpResponse<IMateriales[]>) => res.body ?? []))
      .pipe(
        map((materiales: IMateriales[]) =>
          this.materialesService.addMaterialesToCollectionIfMissing<IMateriales>(materiales, this.prestamos?.materiales)
        )
      )
      .subscribe((materiales: IMateriales[]) => (this.materialesSharedCollection = materiales));
  }
}
