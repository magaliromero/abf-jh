import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICobros } from '../cobros.model';

@Component({
  selector: 'jhi-cobros-detail',
  templateUrl: './cobros-detail.component.html',
})
export class CobrosDetailComponent implements OnInit {
  cobros: ICobros | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cobros }) => {
      this.cobros = cobros;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
