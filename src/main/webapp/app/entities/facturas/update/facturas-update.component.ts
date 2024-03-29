import dayjs from 'dayjs/esm';
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FacturasFormService, FacturasFormGroup } from './facturas-form.service';
import { IFacturas } from '../facturas.model';
import { FacturasService } from '../service/facturas.service';
import { CondicionVenta } from 'app/entities/enumerations/condicion-venta.model';
import { ConsultaClienteService } from 'app/util-services/consulta-cliente.service';
import { EntityArrayResponseType, ProductosService } from 'app/entities/productos/service/productos.service';

@Component({
  selector: 'jhi-facturas-update',
  templateUrl: './facturas-update.component.html',
})
export class FacturasUpdateComponent implements OnInit {
  isSaving = false;
  facturas: IFacturas | null = null;
  productos: any[] = [];
  condicionVentaValues = Object.keys(CondicionVenta);

  editForm: FacturasFormGroup = this.facturasFormService.createFacturasFormGroup();

  nuevoItem: any = {};

  listaDetalle: any[] = [];
  constructor(
    protected facturasService: FacturasService,
    protected facturasFormService: FacturasFormService,
    protected productoService: ProductosService,
    protected activatedRoute: ActivatedRoute,
    protected consultaCliente: ConsultaClienteService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturas }) => {
      this.facturas = facturas;
      if (facturas) {
        this.updateForm(facturas);
      }
      const now = new Date();
      const date = dayjs(now);
      this.editForm.controls.fecha.setValue(date);
    });
    this.queryBackendProductos().subscribe(data => {
      const { body } = data;
      this.productos = body;

      this.editForm.controls.total.disable();
      // this.editForm.controls.fecha.setValue(dayjs().format() )
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facturas = this.facturasFormService.getFacturas(this.editForm);
    const detalle = this.listaDetalle.map((item: any) => {
      return {
        cantidad: item.cantidad,
        precioUnitario: item.precio,
        producto: parseInt(item.producto),
        subtotal: item.subtotal,
      };
    });
    const data = {
      factura: facturas,
      detalle: detalle,
    };
    this.subscribeToSaveResponse(this.facturasService.createNew(data));

    /* if (facturas.id !== null) {
      this.subscribeToSaveResponse(this.facturasService.update(facturas));
    } else {
      this.subscribeToSaveResponse(this.facturasService.create(facturas));
    }*/
  }
  agregarDetalle(): void {
    this.listaDetalle.push(Object.assign({}, this.nuevoItem));
    this.nuevoItem = {};
    this.calcularTotal();
  }
  eliminarDetalle(i: any): void {
    this.listaDetalle.splice(i, 1);
    this.calcularTotal();
  }
  calcularSubtotal(): void {
    this.nuevoItem.subtotal = (this.nuevoItem.cantidad ? this.nuevoItem.cantidad : 0) * (this.nuevoItem.precio ? this.nuevoItem.precio : 0);
  }
  seleccionaProducto(): void {
    const product = this.productos.find(item => item.id == this.nuevoItem.producto);
    if (product) {
      this.nuevoItem.precio = product.precioUnitario;
      this.nuevoItem.iva = product.porcentajeIva;
      this.nuevoItem.descripcionProducto = product.descripcion;
    }
  }

  calcularTotal(): void {
    let total = 0;
    for (let i = 0; i < this.listaDetalle.length; i++) {
      const element = this.listaDetalle[i];
      total += element.subtotal;
    }
    this.editForm.controls.total.setValue(total);
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

  public updateRazonSocial() {
    const ruc = this.editForm.controls.ruc.value;
    this.consultaCliente.consultarCliente(ruc || ' ').subscribe({
      next: (response: any) => {
        this.editForm.controls.razonSocial.setValue(response.body.razonSocial);
        console.log(response);
      },
    });
  }
  protected queryBackendProductos(): Observable<any> {
    const pageToLoad = 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: 100,
      sort: '',
    };
    return this.productoService.query(queryObject);
  }
}
