import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EvaluacionesFormService, EvaluacionesFormGroup } from './evaluaciones-form.service';
import { IEvaluaciones } from '../evaluaciones.model';
import { EvaluacionesService } from '../service/evaluaciones.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { FuncionariosService } from 'app/entities/funcionarios/service/funcionarios.service';
import { TemasService } from 'app/entities/temas/service/temas.service';

@Component({
  selector: 'jhi-evaluaciones-update',
  templateUrl: './evaluaciones-update.component.html',
})
export class EvaluacionesUpdateComponent implements OnInit {
  isSaving = false;
  evaluaciones: IEvaluaciones | null = null;

  alumnosSharedCollection: IAlumnos[] = [];
  funcionariosSharedCollection: IFuncionarios[] = [];

  editForm: EvaluacionesFormGroup = this.evaluacionesFormService.createEvaluacionesFormGroup();
  nuevoItem: any = {};

  listaDetalle: any[] = [];
  temas: any[] = [];

  constructor(
    protected evaluacionesService: EvaluacionesService,
    protected evaluacionesFormService: EvaluacionesFormService,
    protected alumnosService: AlumnosService,
    protected funcionariosService: FuncionariosService,
    protected activatedRoute: ActivatedRoute,
    protected temasService: TemasService
  ) {}

  compareAlumnos = (o1: IAlumnos | null, o2: IAlumnos | null): boolean => this.alumnosService.compareAlumnos(o1, o2);

  compareFuncionarios = (o1: IFuncionarios | null, o2: IFuncionarios | null): boolean =>
    this.funcionariosService.compareFuncionarios(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluaciones }) => {
      this.evaluaciones = evaluaciones;
      if (evaluaciones) {
        this.updateForm(evaluaciones);
      }

      this.loadRelationshipsOptions();
    });

    this.queryBackendTemas().subscribe(data => {
      const { body } = data;
      this.temas = body;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evaluaciones = this.evaluacionesFormService.getEvaluaciones(this.editForm);
    const detalle = this.listaDetalle.map((item: any) => {
      return {
        puntaje: item.puntaje,
        tema: parseInt(item.tema),
        comentarios: item.comentarios,
      };
    });

    const data = {
      cabecera: evaluaciones,
      detalle,
    };

    this.subscribeToSaveResponse(this.evaluacionesService.createDetails(data));
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
    // this.nuevoItem.subtotal = (this.nuevoItem.cantidad ? this.nuevoItem.cantidad : 0) * (this.nuevoItem.precio ? this.nuevoItem.precio : 0);
  }
  selecciona(): void {
    const data = this.temas.find(item => item.id == this.nuevoItem.tema);
    if (data) {
      this.nuevoItem.descripcion = data.descripcion;
    }
  }

  calcularTotal(): void {
    let total = 0;
    for (let i = 0; i < this.listaDetalle.length; i++) {
      const element = this.listaDetalle[i];
      total += element.subtotal;
    }
    //this.editForm.controls.total.setValue(total);
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaluaciones>>): void {
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

  protected updateForm(evaluaciones: IEvaluaciones): void {
    this.evaluaciones = evaluaciones;
    this.evaluacionesFormService.resetForm(this.editForm, evaluaciones);

    this.alumnosSharedCollection = this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(
      this.alumnosSharedCollection,
      evaluaciones.alumnos
    );
    this.funcionariosSharedCollection = this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(
      this.funcionariosSharedCollection,
      evaluaciones.funcionarios
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alumnosService
      .query()
      .pipe(map((res: HttpResponse<IAlumnos[]>) => res.body ?? []))
      .pipe(
        map((alumnos: IAlumnos[]) => this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(alumnos, this.evaluaciones?.alumnos))
      )
      .subscribe((alumnos: IAlumnos[]) => (this.alumnosSharedCollection = alumnos));

    this.funcionariosService
      .query()
      .pipe(map((res: HttpResponse<IFuncionarios[]>) => res.body ?? []))
      .pipe(
        map((funcionarios: IFuncionarios[]) =>
          this.funcionariosService.addFuncionariosToCollectionIfMissing<IFuncionarios>(funcionarios, this.evaluaciones?.funcionarios)
        )
      )
      .subscribe((funcionarios: IFuncionarios[]) => (this.funcionariosSharedCollection = funcionarios));
  }

  protected queryBackendTemas(): Observable<any> {
    const pageToLoad = 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: 100,
      sort: '',
    };

    return this.temasService.query(queryObject);
  }
}
