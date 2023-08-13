import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PuntoDeExpedicionFormService, PuntoDeExpedicionFormGroup } from './punto-de-expedicion-form.service';
import { IPuntoDeExpedicion } from '../punto-de-expedicion.model';
import { PuntoDeExpedicionService } from '../service/punto-de-expedicion.service';
import { ISucursales } from 'app/entities/sucursales/sucursales.model';
import { SucursalesService } from 'app/entities/sucursales/service/sucursales.service';

@Component({
  selector: 'jhi-punto-de-expedicion-update',
  templateUrl: './punto-de-expedicion-update.component.html',
})
export class PuntoDeExpedicionUpdateComponent implements OnInit {
  isSaving = false;
  puntoDeExpedicion: IPuntoDeExpedicion | null = null;

  sucursalesSharedCollection: ISucursales[] = [];

  editForm: PuntoDeExpedicionFormGroup = this.puntoDeExpedicionFormService.createPuntoDeExpedicionFormGroup();

  constructor(
    protected puntoDeExpedicionService: PuntoDeExpedicionService,
    protected puntoDeExpedicionFormService: PuntoDeExpedicionFormService,
    protected sucursalesService: SucursalesService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSucursales = (o1: ISucursales | null, o2: ISucursales | null): boolean => this.sucursalesService.compareSucursales(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ puntoDeExpedicion }) => {
      this.puntoDeExpedicion = puntoDeExpedicion;
      if (puntoDeExpedicion) {
        this.updateForm(puntoDeExpedicion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const puntoDeExpedicion = this.puntoDeExpedicionFormService.getPuntoDeExpedicion(this.editForm);
    if (puntoDeExpedicion.id !== null) {
      this.subscribeToSaveResponse(this.puntoDeExpedicionService.update(puntoDeExpedicion));
    } else {
      this.subscribeToSaveResponse(this.puntoDeExpedicionService.create(puntoDeExpedicion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPuntoDeExpedicion>>): void {
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

  protected updateForm(puntoDeExpedicion: IPuntoDeExpedicion): void {
    this.puntoDeExpedicion = puntoDeExpedicion;
    this.puntoDeExpedicionFormService.resetForm(this.editForm, puntoDeExpedicion);

    this.sucursalesSharedCollection = this.sucursalesService.addSucursalesToCollectionIfMissing<ISucursales>(
      this.sucursalesSharedCollection,
      puntoDeExpedicion.sucursales
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sucursalesService
      .query()
      .pipe(map((res: HttpResponse<ISucursales[]>) => res.body ?? []))
      .pipe(
        map((sucursales: ISucursales[]) =>
          this.sucursalesService.addSucursalesToCollectionIfMissing<ISucursales>(sucursales, this.puntoDeExpedicion?.sucursales)
        )
      )
      .subscribe((sucursales: ISucursales[]) => (this.sucursalesSharedCollection = sucursales));
  }
}
