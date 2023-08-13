import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInscripciones } from '../inscripciones.model';

@Component({
  selector: 'jhi-inscripciones-detail',
  templateUrl: './inscripciones-detail.component.html',
})
export class InscripcionesDetailComponent implements OnInit {
  inscripciones: IInscripciones | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscripciones }) => {
      this.inscripciones = inscripciones;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
