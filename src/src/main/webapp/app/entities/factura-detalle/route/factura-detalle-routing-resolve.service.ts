import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFacturaDetalle } from '../factura-detalle.model';
import { FacturaDetalleService } from '../service/factura-detalle.service';

@Injectable({ providedIn: 'root' })
export class FacturaDetalleRoutingResolveService implements Resolve<IFacturaDetalle | null> {
  constructor(protected service: FacturaDetalleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFacturaDetalle | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((facturaDetalle: HttpResponse<IFacturaDetalle>) => {
          if (facturaDetalle.body) {
            return of(facturaDetalle.body);
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
