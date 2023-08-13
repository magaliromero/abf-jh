import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPuntoDeExpedicion } from '../punto-de-expedicion.model';

@Component({
  selector: 'jhi-punto-de-expedicion-detail',
  templateUrl: './punto-de-expedicion-detail.component.html',
})
export class PuntoDeExpedicionDetailComponent implements OnInit {
  puntoDeExpedicion: IPuntoDeExpedicion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ puntoDeExpedicion }) => {
      this.puntoDeExpedicion = puntoDeExpedicion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
