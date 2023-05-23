import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlumnos } from '../alumnos.model';

@Component({
  selector: 'jhi-alumnos-detail',
  templateUrl: './alumnos-detail.component.html',
})
export class AlumnosDetailComponent implements OnInit {
  alumnos: IAlumnos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alumnos }) => {
      this.alumnos = alumnos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
