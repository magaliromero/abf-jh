import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CursosFormService, CursosFormGroup } from './cursos-form.service';
import { ICursos } from '../cursos.model';
import { CursosService } from '../service/cursos.service';
import { Niveles } from 'app/entities/enumerations/niveles.model';

@Component({
  selector: 'jhi-cursos-update',
  templateUrl: './cursos-update.component.html',
})
export class CursosUpdateComponent implements OnInit {
  isSaving = false;
  cursos: ICursos | null = null;
  nivelesValues = Object.keys(Niveles);

  editForm: CursosFormGroup = this.cursosFormService.createCursosFormGroup();

  constructor(
    protected cursosService: CursosService,
    protected cursosFormService: CursosFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cursos }) => {
      this.cursos = cursos;
      if (cursos) {
        this.updateForm(cursos);
      }
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
  }
}
