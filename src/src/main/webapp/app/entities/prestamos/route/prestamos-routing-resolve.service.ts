import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrestamos } from '../prestamos.model';
import { PrestamosService } from '../service/prestamos.service';

@Injectable({ providedIn: 'root' })
export class PrestamosRoutingResolveService implements Resolve<IPrestamos | null> {
  constructor(protected service: PrestamosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrestamos | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prestamos: HttpResponse<IPrestamos>) => {
          if (prestamos.body) {
            return of(prestamos.body);
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
