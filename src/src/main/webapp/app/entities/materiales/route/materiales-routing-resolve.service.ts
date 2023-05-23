import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMateriales } from '../materiales.model';
import { MaterialesService } from '../service/materiales.service';

@Injectable({ providedIn: 'root' })
export class MaterialesRoutingResolveService implements Resolve<IMateriales | null> {
  constructor(protected service: MaterialesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMateriales | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((materiales: HttpResponse<IMateriales>) => {
          if (materiales.body) {
            return of(materiales.body);
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
