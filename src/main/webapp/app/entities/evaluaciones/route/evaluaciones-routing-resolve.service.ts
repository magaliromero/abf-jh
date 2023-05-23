import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvaluaciones } from '../evaluaciones.model';
import { EvaluacionesService } from '../service/evaluaciones.service';

@Injectable({ providedIn: 'root' })
export class EvaluacionesRoutingResolveService implements Resolve<IEvaluaciones | null> {
  constructor(protected service: EvaluacionesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEvaluaciones | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((evaluaciones: HttpResponse<IEvaluaciones>) => {
          if (evaluaciones.body) {
            return of(evaluaciones.body);
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
