import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductos } from '../productos.model';

@Component({
  selector: 'jhi-productos-detail',
  templateUrl: './productos-detail.component.html',
})
export class ProductosDetailComponent implements OnInit {
  productos: IProductos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productos }) => {
      this.productos = productos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
