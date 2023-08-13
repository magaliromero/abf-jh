import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITimbrados } from '../timbrados.model';

@Component({
  selector: 'jhi-timbrados-detail',
  templateUrl: './timbrados-detail.component.html',
})
export class TimbradosDetailComponent implements OnInit {
  timbrados: ITimbrados | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ timbrados }) => {
      this.timbrados = timbrados;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
