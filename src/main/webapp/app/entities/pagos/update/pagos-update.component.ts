import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PagosFormService, PagosFormGroup } from './pagos-form.service';
import { IPagos } from '../pagos.model';
import { PagosService } from '../service/pagos.service';
import { IProductos } from 'app/entities/productos/productos.model';
import { ProductosService } from 'app/entities/productos/service/productos.service';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { FuncionariosService } from 'app/entities/funcionarios/service/funcionarios.service';

@Component({
  selector: 'jhi-pagos-update',
  templateUrl: './pagos-update.component.html',
})
export class PagosUpdateComponent implements OnInit {
  isSaving = false;
  pagos: IPagos | null = null;

  productosSharedCollection: IProductos[] = [];
  funcionariosSharedCollection: IFuncionarios[] = [];

  editForm: PagosFormGroup = this.pagosFormService.createPagosFormGroup();

  constructor(
    protected pagosService: PagosService,
    protected pagosFormService: PagosFormService,
    protected productosService: ProductosService,
    protected funcionariosService: FuncionariosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProductos = (o1: IProductos | null, o2: IProductos | null): boolean => this.productosService.compareProductos(o1, o2);

  compareFuncionarios = (o1: IFuncionarios | null, o2: IFuncionarios | null): boolean =>
    this.funcionariosService.compareFuncionarios(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pagos }) => {
      this.pagos = pagos;
      if (pagos) {
        this.updateForm(pagos);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pagos = this.pagosFormService.getPagos(this.editForm);
    if (pagos.id !== null) {
      this.subscribeToSaveResponse(this.pagosService.update(pagos));
    } else {
      this.subscribeToSaveResponse(this.pagosService.create(pagos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPagos>>): void {
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

  protected updateForm(pagos: IPagos): void {
    this.pagos = pagos;
    this.pagosFormService.resetForm(this.editForm, pagos);

    this.productosSharedCollection = this.productosService.addProductosToCollectionIfMissing<IProductos>(
      this.productosSharedCollection,
      pagos.producto
    );
    this.funcionariosSharedCollection = this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(
      this.funcionariosSharedCollection,
      pagos.funcionario
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productosService
      .query()
      .pipe(map((res: HttpResponse<IProductos[]>) => res.body ?? []))
      .pipe(
        map((productos: IProductos[]) =>
          this.productosService.addProductosToCollectionIfMissing<IProductos>(productos, this.pagos?.producto)
        )
      )
      .subscribe((productos: IProductos[]) => (this.productosSharedCollection = productos));

    this.funcionariosService
      .query()
      .pipe(map((res: HttpResponse<IFuncionarios[]>) => res.body ?? []))
      .pipe(
        map((funcionarios: IFuncionarios[]) =>
          this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(funcionarios, this.pagos?.funcionario)
        )
      )
      .subscribe((funcionarios: IFuncionarios[]) => (this.funcionariosSharedCollection = funcionarios));
  }
}
