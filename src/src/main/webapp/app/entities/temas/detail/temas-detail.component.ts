import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITemas } from '../temas.model';

@Component({
  selector: 'jhi-temas-detail',
  templateUrl: './temas-detail.component.html',
})
export class TemasDetailComponent implements OnInit {
  temas: ITemas | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temas }) => {
      this.temas = temas;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
