import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPagos } from '../pagos.model';
import { PagosService } from '../service/pagos.service';

@Injectable({ providedIn: 'root' })
export class PagosRoutingResolveService implements Resolve<IPagos | null> {
  constructor(protected service: PagosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPagos | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pagos: HttpResponse<IPagos>) => {
          if (pagos.body) {
            return of(pagos.body);
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
