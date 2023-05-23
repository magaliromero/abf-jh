import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EvaluacionesFormService, EvaluacionesFormGroup } from './evaluaciones-form.service';
import { IEvaluaciones } from '../evaluaciones.model';
import { EvaluacionesService } from '../service/evaluaciones.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { FuncionariosService } from 'app/entities/funcionarios/service/funcionarios.service';

@Component({
  selector: 'jhi-evaluaciones-update',
  templateUrl: './evaluaciones-update.component.html',
})
export class EvaluacionesUpdateComponent implements OnInit {
  isSaving = false;
  evaluaciones: IEvaluaciones | null = null;

  alumnosSharedCollection: IAlumnos[] = [];
  funcionariosSharedCollection: IFuncionarios[] = [];

  editForm: EvaluacionesFormGroup = this.evaluacionesFormService.createEvaluacionesFormGroup();

  constructor(
    protected evaluacionesService: EvaluacionesService,
    protected evaluacionesFormService: EvaluacionesFormService,
    protected alumnosService: AlumnosService,
    protected funcionariosService: FuncionariosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAlumnos = (o1: IAlumnos | null, o2: IAlumnos | null): boolean => this.alumnosService.compareAlumnos(o1, o2);

  compareFuncionarios = (o1: IFuncionarios | null, o2: IFuncionarios | null): boolean =>
    this.funcionariosService.compareFuncionarios(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluaciones }) => {
      this.evaluaciones = evaluaciones;
      if (evaluaciones) {
        this.updateForm(evaluaciones);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evaluaciones = this.evaluacionesFormService.getEvaluaciones(this.editForm);
    if (evaluaciones.id !== null) {
      this.subscribeToSaveResponse(this.evaluacionesService.update(evaluaciones));
    } else {
      this.subscribeToSaveResponse(this.evaluacionesService.create(evaluaciones));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaluaciones>>): void {
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

  protected updateForm(evaluaciones: IEvaluaciones): void {
    this.evaluaciones = evaluaciones;
    this.evaluacionesFormService.resetForm(this.editForm, evaluaciones);

    this.alumnosSharedCollection = this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(
      this.alumnosSharedCollection,
      evaluaciones.alumnos
    );
    this.funcionariosSharedCollection = this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(
      this.funcionariosSharedCollection,
      evaluaciones.funcionarios
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alumnosService
      .query()
      .pipe(map((res: HttpResponse<IAlumnos[]>) => res.body ?? []))
      .pipe(
        map((alumnos: IAlumnos[]) => this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(alumnos, this.evaluaciones?.alumnos))
      )
      .subscribe((alumnos: IAlumnos[]) => (this.alumnosSharedCollection = alumnos));

    this.funcionariosService
      .query()
      .pipe(map((res: HttpResponse<IFuncionarios[]>) => res.body ?? []))
      .pipe(
        map((funcionarios: IFuncionarios[]) =>
          this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(funcionarios, this.evaluaciones?.funcionarios)
        )
      )
      .subscribe((funcionarios: IFuncionarios[]) => (this.funcionariosSharedCollection = funcionarios));
  }
}
