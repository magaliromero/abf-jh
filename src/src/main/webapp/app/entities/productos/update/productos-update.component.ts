import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ProductosFormService, ProductosFormGroup } from './productos-form.service';
import { IProductos } from '../productos.model';
import { ProductosService } from '../service/productos.service';
import { TipoProductos } from 'app/entities/enumerations/tipo-productos.model';

@Component({
  selector: 'jhi-productos-update',
  templateUrl: './productos-update.component.html',
})
export class ProductosUpdateComponent implements OnInit {
  isSaving = false;
  productos: IProductos | null = null;
  tipoProductosValues = Object.keys(TipoProductos);

  editForm: ProductosFormGroup = this.productosFormService.createProductosFormGroup();

  constructor(
    protected productosService: ProductosService,
    protected productosFormService: ProductosFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productos }) => {
      this.productos = productos;
      if (productos) {
        this.updateForm(productos);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productos = this.productosFormService.getProductos(this.editForm);
    if (productos.id !== null) {
      this.subscribeToSaveResponse(this.productosService.update(productos));
    } else {
      this.subscribeToSaveResponse(this.productosService.create(productos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductos>>): void {
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

  protected updateForm(productos: IProductos): void {
    this.productos = productos;
    this.productosFormService.resetForm(this.editForm, productos);
  }
}
