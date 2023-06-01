import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClientes } from '../clientes.model';

@Component({
  selector: 'jhi-clientes-detail',
  templateUrl: './clientes-detail.component.html',
})
export class ClientesDetailComponent implements OnInit {
  clientes: IClientes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clientes }) => {
      this.clientes = clientes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
