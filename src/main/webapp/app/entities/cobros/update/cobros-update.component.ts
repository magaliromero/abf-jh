import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CobrosFormService, CobrosFormGroup } from './cobros-form.service';
import { ICobros } from '../cobros.model';
import { CobrosService } from '../service/cobros.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';
import { IFacturas } from 'app/entities/facturas/facturas.model';
import { FacturasService } from 'app/entities/facturas/service/facturas.service';
import { TiposPagos } from 'app/entities/enumerations/tipos-pagos.model';

@Component({
  selector: 'jhi-cobros-update',
  templateUrl: './cobros-update.component.html',
})
export class CobrosUpdateComponent implements OnInit {
  isSaving = false;
  cobros: ICobros | null = null;
  tiposPagosValues = Object.keys(TiposPagos);

  alumnosSharedCollection: IAlumnos[] = [];
  facturasSharedCollection: IFacturas[] = [];

  editForm: CobrosFormGroup = this.cobrosFormService.createCobrosFormGroup();

  constructor(
    protected cobrosService: CobrosService,
    protected cobrosFormService: CobrosFormService,
    protected alumnosService: AlumnosService,
    protected facturasService: FacturasService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAlumnos = (o1: IAlumnos | null, o2: IAlumnos | null): boolean => this.alumnosService.compareAlumnos(o1, o2);

  compareFacturas = (o1: IFacturas | null, o2: IFacturas | null): boolean => this.facturasService.compareFacturas(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cobros }) => {
      this.cobros = cobros;
      if (cobros) {
        this.updateForm(cobros);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  calcularDiferencia(): void {
    this.editForm.value;
    console.log(this.editForm.value);
    let MontoInicial = this.editForm.value.montoInicial || 0;
    let MontoPago = this.editForm.value.montoPago || 0;
    this.editForm.controls.saldo.setValue(MontoInicial - MontoPago);
  }

  save(): void {
    this.isSaving = true;
    const cobros = this.cobrosFormService.getCobros(this.editForm);
    console.log(cobros);
    /*if (cobros.id !== null) {
      this.subscribeToSaveResponse(this.cobrosService.update(cobros));
    } else {
      this.subscribeToSaveResponse(this.cobrosService.create(cobros));
    }*/
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICobros>>): void {
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

  protected updateForm(cobros: ICobros): void {
    this.cobros = cobros;
    this.cobrosFormService.resetForm(this.editForm, cobros);

    this.alumnosSharedCollection = this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(
      this.alumnosSharedCollection,
      cobros.alumnos
    );
    this.facturasSharedCollection = this.facturasService.addFacturasToCollectionIfMissing<IFacturas>(
      this.facturasSharedCollection,
      cobros.factura
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alumnosService
      .query()
      .pipe(map((res: HttpResponse<IAlumnos[]>) => res.body ?? []))
      .pipe(map((alumnos: IAlumnos[]) => this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(alumnos, this.cobros?.alumnos)))
      .subscribe((alumnos: IAlumnos[]) => (this.alumnosSharedCollection = alumnos));

    this.facturasService
      .query()
      .pipe(map((res: HttpResponse<IFacturas[]>) => res.body ?? []))
      .pipe(
        map((facturas: IFacturas[]) => this.facturasService.addFacturasToCollectionIfMissing<IFacturas>(facturas, this.cobros?.factura))
      )
      .subscribe((facturas: IFacturas[]) => (this.facturasSharedCollection = facturas));
  }
}
