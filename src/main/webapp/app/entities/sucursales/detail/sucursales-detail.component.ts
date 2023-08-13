import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISucursales } from '../sucursales.model';

@Component({
  selector: 'jhi-sucursales-detail',
  templateUrl: './sucursales-detail.component.html',
})
export class SucursalesDetailComponent implements OnInit {
  sucursales: ISucursales | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sucursales }) => {
      this.sucursales = sucursales;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
