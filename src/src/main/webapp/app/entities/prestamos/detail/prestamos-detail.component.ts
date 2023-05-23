import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrestamos } from '../prestamos.model';

@Component({
  selector: 'jhi-prestamos-detail',
  templateUrl: './prestamos-detail.component.html',
})
export class PrestamosDetailComponent implements OnInit {
  prestamos: IPrestamos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestamos }) => {
      this.prestamos = prestamos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
