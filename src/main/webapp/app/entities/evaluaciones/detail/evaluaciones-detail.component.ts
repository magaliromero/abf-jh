import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvaluaciones } from '../evaluaciones.model';

@Component({
  selector: 'jhi-evaluaciones-detail',
  templateUrl: './evaluaciones-detail.component.html',
})
export class EvaluacionesDetailComponent implements OnInit {
  evaluaciones: IEvaluaciones | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluaciones }) => {
      this.evaluaciones = evaluaciones;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
