import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AlumnosFormService, AlumnosFormGroup } from './alumnos-form.service';
import { IAlumnos } from '../alumnos.model';
import { AlumnosService } from '../service/alumnos.service';
import { ITiposDocumentos } from 'app/entities/tipos-documentos/tipos-documentos.model';
import { TiposDocumentosService } from 'app/entities/tipos-documentos/service/tipos-documentos.service';
import { EstadosPersona } from 'app/entities/enumerations/estados-persona.model';

@Component({
  selector: 'jhi-alumnos-update',
  templateUrl: './alumnos-update.component.html',
})
export class AlumnosUpdateComponent implements OnInit {
  isSaving = false;
  alumnos: IAlumnos | null = null;
  estadosPersonaValues = Object.keys(EstadosPersona);

  tiposDocumentosSharedCollection: ITiposDocumentos[] = [];

  editForm: AlumnosFormGroup = this.alumnosFormService.createAlumnosFormGroup();

  constructor(
    protected alumnosService: AlumnosService,
    protected alumnosFormService: AlumnosFormService,
    protected tiposDocumentosService: TiposDocumentosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTiposDocumentos = (o1: ITiposDocumentos | null, o2: ITiposDocumentos | null): boolean =>
    this.tiposDocumentosService.compareTiposDocumentos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alumnos }) => {
      this.alumnos = alumnos;
      if (alumnos) {
        this.updateForm(alumnos);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alumnos = this.alumnosFormService.getAlumnos(this.editForm);
    if (alumnos.id !== null) {
      this.subscribeToSaveResponse(this.alumnosService.update(alumnos));
    } else {
      this.subscribeToSaveResponse(this.alumnosService.create(alumnos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlumnos>>): void {
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

  protected updateForm(alumnos: IAlumnos): void {
    this.alumnos = alumnos;
    this.alumnosFormService.resetForm(this.editForm, alumnos);

    this.tiposDocumentosSharedCollection = this.tiposDocumentosService.addTiposDocumentosToCollectionIfMissing<ITiposDocumentos>(
      this.tiposDocumentosSharedCollection,
      alumnos.tipoDocumentos
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tiposDocumentosService
      .query()
      .pipe(map((res: HttpResponse<ITiposDocumentos[]>) => res.body ?? []))
      .pipe(
        map((tiposDocumentos: ITiposDocumentos[]) =>
          this.tiposDocumentosService.addTiposDocumentosToCollectionIfMissing<ITiposDocumentos>(
            tiposDocumentos,
            this.alumnos?.tipoDocumentos
          )
        )
      )
      .subscribe((tiposDocumentos: ITiposDocumentos[]) => (this.tiposDocumentosSharedCollection = tiposDocumentos));
  }
}
