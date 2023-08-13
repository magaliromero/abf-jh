import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICursos } from '../cursos.model';
import { CursosService } from '../service/cursos.service';

@Injectable({ providedIn: 'root' })
export class CursosRoutingResolveService implements Resolve<ICursos | null> {
  constructor(protected service: CursosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICursos | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cursos: HttpResponse<ICursos>) => {
          if (cursos.body) {
            return of(cursos.body);
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
