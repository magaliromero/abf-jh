import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EvaluacionesDetalleFormService, EvaluacionesDetalleFormGroup } from './evaluaciones-detalle-form.service';
import { IEvaluacionesDetalle } from '../evaluaciones-detalle.model';
import { EvaluacionesDetalleService } from '../service/evaluaciones-detalle.service';
import { IEvaluaciones } from 'app/entities/evaluaciones/evaluaciones.model';
import { EvaluacionesService } from 'app/entities/evaluaciones/service/evaluaciones.service';
import { ITemas } from 'app/entities/temas/temas.model';
import { TemasService } from 'app/entities/temas/service/temas.service';

@Component({
  selector: 'jhi-evaluaciones-detalle-update',
  templateUrl: './evaluaciones-detalle-update.component.html',
})
export class EvaluacionesDetalleUpdateComponent implements OnInit {
  isSaving = false;
  evaluacionesDetalle: IEvaluacionesDetalle | null = null;

  evaluacionesSharedCollection: IEvaluaciones[] = [];
  temasSharedCollection: ITemas[] = [];

  editForm: EvaluacionesDetalleFormGroup = this.evaluacionesDetalleFormService.createEvaluacionesDetalleFormGroup();

  constructor(
    protected evaluacionesDetalleService: EvaluacionesDetalleService,
    protected evaluacionesDetalleFormService: EvaluacionesDetalleFormService,
    protected evaluacionesService: EvaluacionesService,
    protected temasService: TemasService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEvaluaciones = (o1: IEvaluaciones | null, o2: IEvaluaciones | null): boolean =>
    this.evaluacionesService.compareEvaluaciones(o1, o2);

  compareTemas = (o1: ITemas | null, o2: ITemas | null): boolean => this.temasService.compareTemas(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluacionesDetalle }) => {
      this.evaluacionesDetalle = evaluacionesDetalle;
      if (evaluacionesDetalle) {
        this.updateForm(evaluacionesDetalle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evaluacionesDetalle = this.evaluacionesDetalleFormService.getEvaluacionesDetalle(this.editForm);
    if (evaluacionesDetalle.id !== null) {
      this.subscribeToSaveResponse(this.evaluacionesDetalleService.update(evaluacionesDetalle));
    } else {
      this.subscribeToSaveResponse(this.evaluacionesDetalleService.create(evaluacionesDetalle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaluacionesDetalle>>): void {
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

  protected updateForm(evaluacionesDetalle: IEvaluacionesDetalle): void {
    this.evaluacionesDetalle = evaluacionesDetalle;
    this.evaluacionesDetalleFormService.resetForm(this.editForm, evaluacionesDetalle);

    this.evaluacionesSharedCollection = this.evaluacionesService.addEvaluacionesToCollectionIfMissing<IEvaluaciones>(
      this.evaluacionesSharedCollection,
      evaluacionesDetalle.evaluaciones
    );
    this.temasSharedCollection = this.temasService.addTemasToCollectionIfMissing<ITemas>(
      this.temasSharedCollection,
      evaluacionesDetalle.temas
    );
  }

  protected loadRelationshipsOptions(): void {
    this.evaluacionesService
      .query()
      .pipe(map((res: HttpResponse<IEvaluaciones[]>) => res.body ?? []))
      .pipe(
        map((evaluaciones: IEvaluaciones[]) =>
          this.evaluacionesService.addEvaluacionesToCollectionIfMissing<IEvaluaciones>(evaluaciones, this.evaluacionesDetalle?.evaluaciones)
        )
      )
      .subscribe((evaluaciones: IEvaluaciones[]) => (this.evaluacionesSharedCollection = evaluaciones));

    this.temasService
      .query()
      .pipe(map((res: HttpResponse<ITemas[]>) => res.body ?? []))
      .pipe(map((temas: ITemas[]) => this.temasService.addTemasToCollectionIfMissing<ITemas>(temas, this.evaluacionesDetalle?.temas)))
      .subscribe((temas: ITemas[]) => (this.temasSharedCollection = temas));
  }
}
