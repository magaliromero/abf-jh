import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FacturaDetalleFormService, FacturaDetalleFormGroup } from './factura-detalle-form.service';
import { IFacturaDetalle } from '../factura-detalle.model';
import { FacturaDetalleService } from '../service/factura-detalle.service';
import { IProductos } from 'app/entities/productos/productos.model';
import { ProductosService } from 'app/entities/productos/service/productos.service';
import { IFacturas } from 'app/entities/facturas/facturas.model';
import { FacturasService } from 'app/entities/facturas/service/facturas.service';

@Component({
  selector: 'jhi-factura-detalle-update',
  templateUrl: './factura-detalle-update.component.html',
})
export class FacturaDetalleUpdateComponent implements OnInit {
  isSaving = false;
  facturaDetalle: IFacturaDetalle | null = null;

  productosSharedCollection: IProductos[] = [];
  facturasSharedCollection: IFacturas[] = [];

  editForm: FacturaDetalleFormGroup = this.facturaDetalleFormService.createFacturaDetalleFormGroup();

  constructor(
    protected facturaDetalleService: FacturaDetalleService,
    protected facturaDetalleFormService: FacturaDetalleFormService,
    protected productosService: ProductosService,
    protected facturasService: FacturasService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProductos = (o1: IProductos | null, o2: IProductos | null): boolean => this.productosService.compareProductos(o1, o2);

  compareFacturas = (o1: IFacturas | null, o2: IFacturas | null): boolean => this.facturasService.compareFacturas(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturaDetalle }) => {
      this.facturaDetalle = facturaDetalle;
      if (facturaDetalle) {
        this.updateForm(facturaDetalle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facturaDetalle = this.facturaDetalleFormService.getFacturaDetalle(this.editForm);
    if (facturaDetalle.id !== null) {
      this.subscribeToSaveResponse(this.facturaDetalleService.update(facturaDetalle));
    } else {
      this.subscribeToSaveResponse(this.facturaDetalleService.create(facturaDetalle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacturaDetalle>>): void {
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

  protected updateForm(facturaDetalle: IFacturaDetalle): void {
    this.facturaDetalle = facturaDetalle;
    this.facturaDetalleFormService.resetForm(this.editForm, facturaDetalle);

    this.productosSharedCollection = this.productosService.addProductosToCollectionIfMissing<IProductos>(
      this.productosSharedCollection,
      facturaDetalle.producto
    );
    this.facturasSharedCollection = this.facturasService.addFacturasToCollectionIfMissing<IFacturas>(
      this.facturasSharedCollection,
      facturaDetalle.factura
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productosService
      .query()
      .pipe(map((res: HttpResponse<IProductos[]>) => res.body ?? []))
      .pipe(
        map((productos: IProductos[]) =>
          this.productosService.addProductosToCollectionIfMissing<IProductos>(productos, this.facturaDetalle?.producto)
        )
      )
      .subscribe((productos: IProductos[]) => (this.productosSharedCollection = productos));

    this.facturasService
      .query()
      .pipe(map((res: HttpResponse<IFacturas[]>) => res.body ?? []))
      .pipe(
        map((facturas: IFacturas[]) =>
          this.facturasService.addFacturasToCollectionIfMissing<IFacturas>(facturas, this.facturaDetalle?.factura)
        )
      )
      .subscribe((facturas: IFacturas[]) => (this.facturasSharedCollection = facturas));
  }
}
