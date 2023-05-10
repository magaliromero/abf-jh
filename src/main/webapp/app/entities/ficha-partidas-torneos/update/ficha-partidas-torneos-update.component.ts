import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FichaPartidasTorneosFormService, FichaPartidasTorneosFormGroup } from './ficha-partidas-torneos-form.service';
import { IFichaPartidasTorneos } from '../ficha-partidas-torneos.model';
import { FichaPartidasTorneosService } from '../service/ficha-partidas-torneos.service';
import { ITorneos } from 'app/entities/torneos/torneos.model';
import { TorneosService } from 'app/entities/torneos/service/torneos.service';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';

@Component({
  selector: 'jhi-ficha-partidas-torneos-update',
  templateUrl: './ficha-partidas-torneos-update.component.html',
})
export class FichaPartidasTorneosUpdateComponent implements OnInit {
  isSaving = false;
  fichaPartidasTorneos: IFichaPartidasTorneos | null = null;

  torneosSharedCollection: ITorneos[] = [];
  alumnosSharedCollection: IAlumnos[] = [];

  editForm: FichaPartidasTorneosFormGroup = this.fichaPartidasTorneosFormService.createFichaPartidasTorneosFormGroup();

  constructor(
    protected fichaPartidasTorneosService: FichaPartidasTorneosService,
    protected fichaPartidasTorneosFormService: FichaPartidasTorneosFormService,
    protected torneosService: TorneosService,
    protected alumnosService: AlumnosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTorneos = (o1: ITorneos | null, o2: ITorneos | null): boolean => this.torneosService.compareTorneos(o1, o2);

  compareAlumnos = (o1: IAlumnos | null, o2: IAlumnos | null): boolean => this.alumnosService.compareAlumnos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fichaPartidasTorneos }) => {
      this.fichaPartidasTorneos = fichaPartidasTorneos;
      if (fichaPartidasTorneos) {
        this.updateForm(fichaPartidasTorneos);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fichaPartidasTorneos = this.fichaPartidasTorneosFormService.getFichaPartidasTorneos(this.editForm);
    if (fichaPartidasTorneos.id !== null) {
      this.subscribeToSaveResponse(this.fichaPartidasTorneosService.update(fichaPartidasTorneos));
    } else {
      this.subscribeToSaveResponse(this.fichaPartidasTorneosService.create(fichaPartidasTorneos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFichaPartidasTorneos>>): void {
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

  protected updateForm(fichaPartidasTorneos: IFichaPartidasTorneos): void {
    this.fichaPartidasTorneos = fichaPartidasTorneos;
    this.fichaPartidasTorneosFormService.resetForm(this.editForm, fichaPartidasTorneos);

    this.torneosSharedCollection = this.torneosService.addTorneosToCollectionIfMissing<ITorneos>(
      this.torneosSharedCollection,
      fichaPartidasTorneos.torneos
    );
    this.alumnosSharedCollection = this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(
      this.alumnosSharedCollection,
      fichaPartidasTorneos.alumnos
    );
  }

  protected loadRelationshipsOptions(): void {
    this.torneosService
      .query()
      .pipe(map((res: HttpResponse<ITorneos[]>) => res.body ?? []))
      .pipe(
        map((torneos: ITorneos[]) =>
          this.torneosService.addTorneosToCollectionIfMissing<ITorneos>(torneos, this.fichaPartidasTorneos?.torneos)
        )
      )
      .subscribe((torneos: ITorneos[]) => (this.torneosSharedCollection = torneos));

    this.alumnosService
      .query()
      .pipe(map((res: HttpResponse<IAlumnos[]>) => res.body ?? []))
      .pipe(
        map((alumnos: IAlumnos[]) =>
          this.alumnosService.addAlumnosToCollectionIfMissing<IAlumnos>(alumnos, this.fichaPartidasTorneos?.alumnos)
        )
      )
      .subscribe((alumnos: IAlumnos[]) => (this.alumnosSharedCollection = alumnos));
  }
}
