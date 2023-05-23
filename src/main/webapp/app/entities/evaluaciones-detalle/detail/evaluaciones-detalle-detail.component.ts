import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvaluacionesDetalle } from '../evaluaciones-detalle.model';

@Component({
  selector: 'jhi-evaluaciones-detalle-detail',
  templateUrl: './evaluaciones-detalle-detail.component.html',
})
export class EvaluacionesDetalleDetailComponent implements OnInit {
  evaluacionesDetalle: IEvaluacionesDetalle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluacionesDetalle }) => {
      this.evaluacionesDetalle = evaluacionesDetalle;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
