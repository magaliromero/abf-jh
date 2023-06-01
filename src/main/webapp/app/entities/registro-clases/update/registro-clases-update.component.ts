import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RegistroClasesFormService, RegistroClasesFormGroup } from './registro-clases-form.service';
import { IRegistroClases } from '../registro-clases.model';
import { RegistroClasesService } from '../service/registro-clases.service';
import { ITemas } from 'app/entities/temas/temas.model';
import { TemasService } from 'app/entities/temas/service/temas.service';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { FuncionariosService } from 'app/entities/funcionarios/service/funcionarios.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';

@Component({
  selector: 'jhi-registro-clases-update',
  templateUrl: './registro-clases-update.component.html',
})
export class RegistroClasesUpdateComponent implements OnInit {
  isSaving = false;
  registroClases: IRegistroClases | null = null;

  temasSharedCollection: ITemas[] = [];
  funcionariosSharedCollection: IFuncionarios[] = [];
  alumnosSharedCollection: IAlumnos[] = [];

  editForm: RegistroClasesFormGroup = this.registroClasesFormService.createRegistroClasesFormGroup();

  constructor(
    protected registroClasesService: RegistroClasesService,
    protected registroClasesFormService: RegistroClasesFormService,
    protected temasService: TemasService,
    protected funcionariosService: FuncionariosService,
    protected alumnosService: AlumnosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTemas = (o1: ITemas | null, o2: ITemas | null): boolean => this.temasService.compareTemas(o1, o2);

  compareFuncionarios = (o1: IFuncionarios | null, o2: IFuncionarios | null): boolean =>
    this.funcionariosService.compareFuncionarios(o1, o2);

  compareAlumnos = (o1: IAlumnos | null, o2: IAlumnos | null): boolean => this.alumnosService.compareAlumnos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registroClases }) => {
      this.registroClases = registroClases;
      if (registroClases) {
        this.updateForm(registroClases);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registroClases = this.registroClasesFormService.getRegistroClases(this.editForm);
    if (registroClases.id !== null) {
      this.subscribeToSaveResponse(this.registroClasesService.update(registroClases));
    } else {
      this.subscribeToSaveResponse(this.registroClasesService.create(registroClases));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegistroClases>>): void {
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

  protected updateForm(registroClases: IRegistroClases): void {
    this.registroClases = registroClases;
    this.registroClasesFormService.resetForm(this.editForm, registroClases);

    this.temasSharedCollection = this.temasService.addTemasToCollectionIfMissing<ITemas>(this.temasSharedCollection, registroClases.temas);
    this.funcionariosSharedCollection = this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(
      this.funcionariosSharedCollection,
      registroClases.funcionario
    );
    this.alumnosSharedCollection = this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(
      this.alumnosSharedCollection,
      registroClases.alumnos
    );
  }

  protected loadRelationshipsOptions(): void {
    this.temasService
      .query()
      .pipe(map((res: HttpResponse<ITemas[]>) => res.body ?? []))
      .pipe(map((temas: ITemas[]) => this.temasService.addTemasToCollectionIfMissing<ITemas>(temas, this.registroClases?.temas)))
      .subscribe((temas: ITemas[]) => (this.temasSharedCollection = temas));

    this.funcionariosService
      .query()
      .pipe(map((res: HttpResponse<IFuncionarios[]>) => res.body ?? []))
      .pipe(
        map((funcionarios: IFuncionarios[]) =>
          this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(funcionarios, this.registroClases?.funcionario)
        )
      )
      .subscribe((funcionarios: IFuncionarios[]) => (this.funcionariosSharedCollection = funcionarios));

    this.alumnosService
      .query()
      .pipe(map((res: HttpResponse<IAlumnos[]>) => res.body ?? []))
      .pipe(
        map((alumnos: IAlumnos[]) => this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(alumnos, this.registroClases?.alumnos))
      )
      .subscribe((alumnos: IAlumnos[]) => (this.alumnosSharedCollection = alumnos));
  }
}
