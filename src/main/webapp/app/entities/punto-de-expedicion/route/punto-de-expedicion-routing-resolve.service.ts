import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPuntoDeExpedicion } from '../punto-de-expedicion.model';
import { PuntoDeExpedicionService } from '../service/punto-de-expedicion.service';

@Injectable({ providedIn: 'root' })
export class PuntoDeExpedicionRoutingResolveService implements Resolve<IPuntoDeExpedicion | null> {
  constructor(protected service: PuntoDeExpedicionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPuntoDeExpedicion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((puntoDeExpedicion: HttpResponse<IPuntoDeExpedicion>) => {
          if (puntoDeExpedicion.body) {
            return of(puntoDeExpedicion.body);
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
