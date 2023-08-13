import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITimbrados } from '../timbrados.model';
import { TimbradosService } from '../service/timbrados.service';

@Injectable({ providedIn: 'root' })
export class TimbradosRoutingResolveService implements Resolve<ITimbrados | null> {
  constructor(protected service: TimbradosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITimbrados | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((timbrados: HttpResponse<ITimbrados>) => {
          if (timbrados.body) {
            return of(timbrados.body);
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
