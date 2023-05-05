import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PagosFormService, PagosFormGroup } from './pagos-form.service';
import { IPagos } from '../pagos.model';
import { PagosService } from '../service/pagos.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { FuncionariosService } from 'app/entities/funcionarios/service/funcionarios.service';

@Component({
  selector: 'jhi-pagos-update',
  templateUrl: './pagos-update.component.html',
})
export class PagosUpdateComponent implements OnInit {
  isSaving = false;
  pagos: IPagos | null = null;

  alumnosSharedCollection: IAlumnos[] = [];
  funcionariosSharedCollection: IFuncionarios[] = [];

  editForm: PagosFormGroup = this.pagosFormService.createPagosFormGroup();

  constructor(
    protected pagosService: PagosService,
    protected pagosFormService: PagosFormService,
    protected alumnosService: AlumnosService,
    protected funcionariosService: FuncionariosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAlumnos = (o1: IAlumnos | null, o2: IAlumnos | null): boolean => this.alumnosService.compareAlumnos(o1, o2);

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
  calcularDiferencia(): void {
    this.editForm.value;
    console.log(this.editForm.value);
    let MontoInicial = this.editForm.value.montoInicial || 0;
    let MontoPago = this.editForm.value.montoPago || 0;
    this.editForm.controls.saldo.setValue(MontoInicial - MontoPago);
  }
  save(): void {
    this.isSaving = true;
    const pagos = this.pagosFormService.getPagos(this.editForm);
    console.log(pagos);
    /*if (pagos.id !== null) {
      this.subscribeToSaveResponse(this.pagosService.update(pagos));
    } else {
      this.subscribeToSaveResponse(this.pagosService.create(pagos));
    }*/
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

    this.alumnosSharedCollection = this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(
      this.alumnosSharedCollection,
      pagos.alumnos
    );
    this.funcionariosSharedCollection = this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(
      this.funcionariosSharedCollection,
      pagos.funcionarios
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alumnosService
      .query()
      .pipe(map((res: HttpResponse<IAlumnos[]>) => res.body ?? []))
      .pipe(map((alumnos: IAlumnos[]) => this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(alumnos, this.pagos?.alumnos)))
      .subscribe((alumnos: IAlumnos[]) => (this.alumnosSharedCollection = alumnos));

    this.funcionariosService
      .query()
      .pipe(map((res: HttpResponse<IFuncionarios[]>) => res.body ?? []))
      .pipe(
        map((funcionarios: IFuncionarios[]) =>
          this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(funcionarios, this.pagos?.funcionarios)
        )
      )
      .subscribe((funcionarios: IFuncionarios[]) => (this.funcionariosSharedCollection = funcionarios));
  }
}
