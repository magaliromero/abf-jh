import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MatriculaFormService, MatriculaFormGroup } from './matricula-form.service';
import { IMatricula } from '../matricula.model';
import { MatriculaService } from '../service/matricula.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';
import { EstadosPagos } from 'app/entities/enumerations/estados-pagos.model';

@Component({
  selector: 'jhi-matricula-update',
  templateUrl: './matricula-update.component.html',
})
export class MatriculaUpdateComponent implements OnInit {
  isSaving = false;
  matricula: IMatricula | null = null;
  estadosPagosValues = Object.keys(EstadosPagos);

  alumnosSharedCollection: IAlumnos[] = [];

  editForm: MatriculaFormGroup = this.matriculaFormService.createMatriculaFormGroup();

  constructor(
    protected matriculaService: MatriculaService,
    protected matriculaFormService: MatriculaFormService,
    protected alumnosService: AlumnosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAlumnos = (o1: IAlumnos | null, o2: IAlumnos | null): boolean => this.alumnosService.compareAlumnos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matricula }) => {
      this.matricula = matricula;
      if (matricula) {
        this.updateForm(matricula);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matricula = this.matriculaFormService.getMatricula(this.editForm);
    if (matricula.id !== null) {
      this.subscribeToSaveResponse(this.matriculaService.update(matricula));
    } else {
      this.subscribeToSaveResponse(this.matriculaService.create(matricula));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatricula>>): void {
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

  protected updateForm(matricula: IMatricula): void {
    this.matricula = matricula;
    this.matriculaFormService.resetForm(this.editForm, matricula);

    this.alumnosSharedCollection = this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(
      this.alumnosSharedCollection,
      matricula.alumno
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alumnosService
      .query()
      .pipe(map((res: HttpResponse<IAlumnos[]>) => res.body ?? []))
      .pipe(map((alumnos: IAlumnos[]) => this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(alumnos, this.matricula?.alumno)))
      .subscribe((alumnos: IAlumnos[]) => (this.alumnosSharedCollection = alumnos));
  }
}
