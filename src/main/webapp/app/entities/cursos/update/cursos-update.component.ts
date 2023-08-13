import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CursosFormService, CursosFormGroup } from './cursos-form.service';
import { ICursos } from '../cursos.model';
import { CursosService } from '../service/cursos.service';
import { ITemas } from 'app/entities/temas/temas.model';
import { TemasService } from 'app/entities/temas/service/temas.service';
import { Niveles } from 'app/entities/enumerations/niveles.model';

@Component({
  selector: 'jhi-cursos-update',
  templateUrl: './cursos-update.component.html',
})
export class CursosUpdateComponent implements OnInit {
  isSaving = false;
  cursos: ICursos | null = null;
  nivelesValues = Object.keys(Niveles);

  temasSharedCollection: ITemas[] = [];

  editForm: CursosFormGroup = this.cursosFormService.createCursosFormGroup();

  constructor(
    protected cursosService: CursosService,
    protected cursosFormService: CursosFormService,
    protected temasService: TemasService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTemas = (o1: ITemas | null, o2: ITemas | null): boolean => this.temasService.compareTemas(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cursos }) => {
      this.cursos = cursos;
      if (cursos) {
        this.updateForm(cursos);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cursos = this.cursosFormService.getCursos(this.editForm);
    if (cursos.id !== null) {
      this.subscribeToSaveResponse(this.cursosService.update(cursos));
    } else {
      this.subscribeToSaveResponse(this.cursosService.create(cursos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICursos>>): void {
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

  protected updateForm(cursos: ICursos): void {
    this.cursos = cursos;
    this.cursosFormService.resetForm(this.editForm, cursos);

    this.temasSharedCollection = this.temasService.addTemasToCollectionIfMissing<ITemas>(this.temasSharedCollection, cursos.temas);
  }

  protected loadRelationshipsOptions(): void {
    this.temasService
      .query()
      .pipe(map((res: HttpResponse<ITemas[]>) => res.body ?? []))
      .pipe(map((temas: ITemas[]) => this.temasService.addTemasToCollectionIfMissing<ITemas>(temas, this.cursos?.temas)))
      .subscribe((temas: ITemas[]) => (this.temasSharedCollection = temas));
  }
}
