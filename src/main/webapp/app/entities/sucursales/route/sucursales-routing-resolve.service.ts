import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISucursales } from '../sucursales.model';
import { SucursalesService } from '../service/sucursales.service';

@Injectable({ providedIn: 'root' })
export class SucursalesRoutingResolveService implements Resolve<ISucursales | null> {
  constructor(protected service: SucursalesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISucursales | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sucursales: HttpResponse<ISucursales>) => {
          if (sucursales.body) {
            return of(sucursales.body);
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
