import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductos } from '../productos.model';
import { ProductosService } from '../service/productos.service';

@Injectable({ providedIn: 'root' })
export class ProductosRoutingResolveService implements Resolve<IProductos | null> {
  constructor(protected service: ProductosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductos | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productos: HttpResponse<IProductos>) => {
          if (productos.body) {
            return of(productos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
