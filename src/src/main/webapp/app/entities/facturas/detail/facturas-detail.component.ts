import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFacturas } from '../facturas.model';

@Component({
  selector: 'jhi-facturas-detail',
  templateUrl: './facturas-detail.component.html',
})
export class FacturasDetailComponent implements OnInit {
  facturas: IFacturas | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facturas }) => {
      this.facturas = facturas;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
