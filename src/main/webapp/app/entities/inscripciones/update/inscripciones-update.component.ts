import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { InscripcionesFormService, InscripcionesFormGroup } from './inscripciones-form.service';
import { IInscripciones } from '../inscripciones.model';
import { InscripcionesService } from '../service/inscripciones.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';
import { ICursos } from 'app/entities/cursos/cursos.model';
import { CursosService } from 'app/entities/cursos/service/cursos.service';

@Component({
  selector: 'jhi-inscripciones-update',
  templateUrl: './inscripciones-update.component.html',
})
export class InscripcionesUpdateComponent implements OnInit {
  isSaving = false;
  inscripciones: IInscripciones | null = null;

  alumnosSharedCollection: IAlumnos[] = [];
  cursosSharedCollection: ICursos[] = [];

  editForm: InscripcionesFormGroup = this.inscripcionesFormService.createInscripcionesFormGroup();

  constructor(
    protected inscripcionesService: InscripcionesService,
    protected inscripcionesFormService: InscripcionesFormService,
    protected alumnosService: AlumnosService,
    protected cursosService: CursosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAlumnos = (o1: IAlumnos | null, o2: IAlumnos | null): boolean => this.alumnosService.compareAlumnos(o1, o2);

  compareCursos = (o1: ICursos | null, o2: ICursos | null): boolean => this.cursosService.compareCursos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscripciones }) => {
      this.inscripciones = inscripciones;
      if (inscripciones) {
        this.updateForm(inscripciones);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscripciones = this.inscripcionesFormService.getInscripciones(this.editForm);
    if (inscripciones.id !== null) {
      this.subscribeToSaveResponse(this.inscripcionesService.update(inscripciones));
    } else {
      this.subscribeToSaveResponse(this.inscripcionesService.create(inscripciones));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscripciones>>): void {
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

  protected updateForm(inscripciones: IInscripciones): void {
    this.inscripciones = inscripciones;
    this.inscripcionesFormService.resetForm(this.editForm, inscripciones);

    this.alumnosSharedCollection = this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(
      this.alumnosSharedCollection,
      inscripciones.alumnos
    );
    this.cursosSharedCollection = this.cursosService.addCursosToCollectionIfMissing<ICursos>(
      this.cursosSharedCollection,
      inscripciones.cursos
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alumnosService
      .query()
      .pipe(map((res: HttpResponse<IAlumnos[]>) => res.body ?? []))
      .pipe(
        map((alumnos: IAlumnos[]) => this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(alumnos, this.inscripciones?.alumnos))
      )
      .subscribe((alumnos: IAlumnos[]) => (this.alumnosSharedCollection = alumnos));

    this.cursosService
      .query()
      .pipe(map((res: HttpResponse<ICursos[]>) => res.body ?? []))
      .pipe(map((cursos: ICursos[]) => this.cursosService.addCursosToCollectionIfMissing<ICursos>(cursos, this.inscripciones?.cursos)))
      .subscribe((cursos: ICursos[]) => (this.cursosSharedCollection = cursos));
  }
}
