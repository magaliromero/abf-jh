import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITemas } from '../temas.model';
import { TemasService } from '../service/temas.service';

@Injectable({ providedIn: 'root' })
export class TemasRoutingResolveService implements Resolve<ITemas | null> {
  constructor(protected service: TemasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITemas | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((temas: HttpResponse<ITemas>) => {
          if (temas.body) {
            return of(temas.body);
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
