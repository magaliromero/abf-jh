import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TiposDocumentosFormService, TiposDocumentosFormGroup } from './tipos-documentos-form.service';
import { ITiposDocumentos } from '../tipos-documentos.model';
import { TiposDocumentosService } from '../service/tipos-documentos.service';

@Component({
  selector: 'jhi-tipos-documentos-update',
  templateUrl: './tipos-documentos-update.component.html',
})
export class TiposDocumentosUpdateComponent implements OnInit {
  isSaving = false;
  tiposDocumentos: ITiposDocumentos | null = null;

  editForm: TiposDocumentosFormGroup = this.tiposDocumentosFormService.createTiposDocumentosFormGroup();

  constructor(
    protected tiposDocumentosService: TiposDocumentosService,
    protected tiposDocumentosFormService: TiposDocumentosFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tiposDocumentos }) => {
      this.tiposDocumentos = tiposDocumentos;
      if (tiposDocumentos) {
        this.updateForm(tiposDocumentos);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tiposDocumentos = this.tiposDocumentosFormService.getTiposDocumentos(this.editForm);
    if (tiposDocumentos.id !== null) {
      this.subscribeToSaveResponse(this.tiposDocumentosService.update(tiposDocumentos));
    } else {
      this.subscribeToSaveResponse(this.tiposDocumentosService.create(tiposDocumentos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITiposDocumentos>>): void {
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

  protected updateForm(tiposDocumentos: ITiposDocumentos): void {
    this.tiposDocumentos = tiposDocumentos;
    this.tiposDocumentosFormService.resetForm(this.editForm, tiposDocumentos);
  }
}
