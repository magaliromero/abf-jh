import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SucursalesFormService, SucursalesFormGroup } from './sucursales-form.service';
import { ISucursales } from '../sucursales.model';
import { SucursalesService } from '../service/sucursales.service';

@Component({
  selector: 'jhi-sucursales-update',
  templateUrl: './sucursales-update.component.html',
})
export class SucursalesUpdateComponent implements OnInit {
  isSaving = false;
  sucursales: ISucursales | null = null;

  editForm: SucursalesFormGroup = this.sucursalesFormService.createSucursalesFormGroup();

  constructor(
    protected sucursalesService: SucursalesService,
    protected sucursalesFormService: SucursalesFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sucursales }) => {
      this.sucursales = sucursales;
      if (sucursales) {
        this.updateForm(sucursales);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sucursales = this.sucursalesFormService.getSucursales(this.editForm);
    if (sucursales.id !== null) {
      this.subscribeToSaveResponse(this.sucursalesService.update(sucursales));
    } else {
      this.subscribeToSaveResponse(this.sucursalesService.create(sucursales));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISucursales>>): void {
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

  protected updateForm(sucursales: ISucursales): void {
    this.sucursales = sucursales;
    this.sucursalesFormService.resetForm(this.editForm, sucursales);
  }
}
