import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvaluacionesDetalle } from '../evaluaciones-detalle.model';
import { EvaluacionesDetalleService } from '../service/evaluaciones-detalle.service';

@Injectable({ providedIn: 'root' })
export class EvaluacionesDetalleRoutingResolveService implements Resolve<IEvaluacionesDetalle | null> {
  constructor(protected service: EvaluacionesDetalleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEvaluacionesDetalle | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((evaluacionesDetalle: HttpResponse<IEvaluacionesDetalle>) => {
          if (evaluacionesDetalle.body) {
            return of(evaluacionesDetalle.body);
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
