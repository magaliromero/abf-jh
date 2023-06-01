import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ClientesFormService, ClientesFormGroup } from './clientes-form.service';
import { IClientes } from '../clientes.model';
import { ClientesService } from '../service/clientes.service';

@Component({
  selector: 'jhi-clientes-update',
  templateUrl: './clientes-update.component.html',
})
export class ClientesUpdateComponent implements OnInit {
  isSaving = false;
  clientes: IClientes | null = null;

  editForm: ClientesFormGroup = this.clientesFormService.createClientesFormGroup();

  constructor(
    protected clientesService: ClientesService,
    protected clientesFormService: ClientesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clientes }) => {
      this.clientes = clientes;
      if (clientes) {
        this.updateForm(clientes);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clientes = this.clientesFormService.getClientes(this.editForm);
    if (clientes.id !== null) {
      this.subscribeToSaveResponse(this.clientesService.update(clientes));
    } else {
      this.subscribeToSaveResponse(this.clientesService.create(clientes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClientes>>): void {
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

  protected updateForm(clientes: IClientes): void {
    this.clientes = clientes;
    this.clientesFormService.resetForm(this.editForm, clientes);
  }
}
