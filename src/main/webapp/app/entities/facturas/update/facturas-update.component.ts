import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FacturasFormService, FacturasFormGroup } from './facturas-form.service';
import { IFacturas } from '../facturas.model';
import { FacturasService } from '../service/facturas.service';
import { CondicionVenta } from 'app/entities/enumerations/condicion-venta.model';

@Component({
  selector: 'jhi-facturas-update',
  templateUrl: './facturas-update.component.html',
})
export class FacturasUpdateComponent implements OnInit {
  isSaving = false;
  facturas: IFacturas | null = null;
  condicionVentaValues = Object.keys(CondicionVenta);

  editForm: FacturasFormGroup = this.facturasFormService.createFacturasFormGroup();

  constructor(
    protected facturasService: FacturasService,
    protected facturasFormService: FacturasFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturas }) => {
      this.facturas = facturas;
      if (facturas) {
        this.updateForm(facturas);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facturas = this.facturasFormService.getFacturas(this.editForm);
    if (facturas.id !== null) {
      this.subscribeToSaveResponse(this.facturasService.update(facturas));
    } else {
      this.subscribeToSaveResponse(this.facturasService.create(facturas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacturas>>): void {
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

  protected updateForm(facturas: IFacturas): void {
    this.facturas = facturas;
    this.facturasFormService.resetForm(this.editForm, facturas);
  }
}
