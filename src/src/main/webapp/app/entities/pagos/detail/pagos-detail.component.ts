import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPagos } from '../pagos.model';

@Component({
  selector: 'jhi-pagos-detail',
  templateUrl: './pagos-detail.component.html',
})
export class PagosDetailComponent implements OnInit {
  pagos: IPagos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pagos }) => {
      this.pagos = pagos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
