import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICobros } from '../cobros.model';
import { CobrosService } from '../service/cobros.service';

@Injectable({ providedIn: 'root' })
export class CobrosRoutingResolveService implements Resolve<ICobros | null> {
  constructor(protected service: CobrosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICobros | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cobros: HttpResponse<ICobros>) => {
          if (cobros.body) {
            return of(cobros.body);
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
