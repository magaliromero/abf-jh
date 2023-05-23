import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFacturas } from '../facturas.model';
import { FacturasService } from '../service/facturas.service';

@Injectable({ providedIn: 'root' })
export class FacturasRoutingResolveService implements Resolve<IFacturas | null> {
  constructor(protected service: FacturasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFacturas | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((facturas: HttpResponse<IFacturas>) => {
          if (facturas.body) {
            return of(facturas.body);
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
