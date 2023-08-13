import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICursos } from '../cursos.model';

@Component({
  selector: 'jhi-cursos-detail',
  templateUrl: './cursos-detail.component.html',
})
export class CursosDetailComponent implements OnInit {
  cursos: ICursos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cursos }) => {
      this.cursos = cursos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
