import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TorneosFormService, TorneosFormGroup } from './torneos-form.service';
import { ITorneos } from '../torneos.model';
import { TorneosService } from '../service/torneos.service';
import { TiposTorneos } from 'app/entities/enumerations/tipos-torneos.model';

@Component({
  selector: 'jhi-torneos-update',
  templateUrl: './torneos-update.component.html',
})
export class TorneosUpdateComponent implements OnInit {
  isSaving = false;
  torneos: ITorneos | null = null;
  tiposTorneosValues = Object.keys(TiposTorneos);

  editForm: TorneosFormGroup = this.torneosFormService.createTorneosFormGroup();

  constructor(
    protected torneosService: TorneosService,
    protected torneosFormService: TorneosFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ torneos }) => {
      this.torneos = torneos;
      if (torneos) {
        this.updateForm(torneos);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const torneos = this.torneosFormService.getTorneos(this.editForm);
    if (torneos.id !== null) {
      this.subscribeToSaveResponse(this.torneosService.update(torneos));
    } else {
      this.subscribeToSaveResponse(this.torneosService.create(torneos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITorneos>>): void {
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

  protected updateForm(torneos: ITorneos): void {
    this.torneos = torneos;
    this.torneosFormService.resetForm(this.editForm, torneos);
  }
}
