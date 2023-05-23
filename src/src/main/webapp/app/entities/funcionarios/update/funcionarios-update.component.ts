import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FuncionariosFormService, FuncionariosFormGroup } from './funcionarios-form.service';
import { IFuncionarios } from '../funcionarios.model';
import { FuncionariosService } from '../service/funcionarios.service';
import { ITiposDocumentos } from 'app/entities/tipos-documentos/tipos-documentos.model';
import { TiposDocumentosService } from 'app/entities/tipos-documentos/service/tipos-documentos.service';
import { EstadosPersona } from 'app/entities/enumerations/estados-persona.model';
import { TipoFuncionarios } from 'app/entities/enumerations/tipo-funcionarios.model';

@Component({
  selector: 'jhi-funcionarios-update',
  templateUrl: './funcionarios-update.component.html',
})
export class FuncionariosUpdateComponent implements OnInit {
  isSaving = false;
  funcionarios: IFuncionarios | null = null;
  estadosPersonaValues = Object.keys(EstadosPersona);
  tipoFuncionariosValues = Object.keys(TipoFuncionarios);

  tiposDocumentosSharedCollection: ITiposDocumentos[] = [];

  editForm: FuncionariosFormGroup = this.funcionariosFormService.createFuncionariosFormGroup();

  constructor(
    protected funcionariosService: FuncionariosService,
    protected funcionariosFormService: FuncionariosFormService,
    protected tiposDocumentosService: TiposDocumentosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTiposDocumentos = (o1: ITiposDocumentos | null, o2: ITiposDocumentos | null): boolean =>
    this.tiposDocumentosService.compareTiposDocumentos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionarios }) => {
      this.funcionarios = funcionarios;
      if (funcionarios) {
        this.updateForm(funcionarios);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const funcionarios = this.funcionariosFormService.getFuncionarios(this.editForm);
    if (funcionarios.id !== null) {
      this.subscribeToSaveResponse(this.funcionariosService.update(funcionarios));
    } else {
      this.subscribeToSaveResponse(this.funcionariosService.create(funcionarios));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuncionarios>>): void {
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

  protected updateForm(funcionarios: IFuncionarios): void {
    this.funcionarios = funcionarios;
    this.funcionariosFormService.resetForm(this.editForm, funcionarios);

    this.tiposDocumentosSharedCollection = this.tiposDocumentosService.addTiposDocumentosToCollectionIfMissing<ITiposDocumentos>(
      this.tiposDocumentosSharedCollection,
      funcionarios.tipoDocumentos
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
            this.funcionarios?.tipoDocumentos
          )
        )
      )
      .subscribe((tiposDocumentos: ITiposDocumentos[]) => (this.tiposDocumentosSharedCollection = tiposDocumentos));
  }
}
