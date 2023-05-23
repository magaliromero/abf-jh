import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFuncionarios } from '../funcionarios.model';

@Component({
  selector: 'jhi-funcionarios-detail',
  templateUrl: './funcionarios-detail.component.html',
})
export class FuncionariosDetailComponent implements OnInit {
  funcionarios: IFuncionarios | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionarios }) => {
      this.funcionarios = funcionarios;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
