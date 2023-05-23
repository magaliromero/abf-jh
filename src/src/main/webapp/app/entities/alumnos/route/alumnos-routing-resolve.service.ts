import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlumnos } from '../alumnos.model';
import { AlumnosService } from '../service/alumnos.service';

@Injectable({ providedIn: 'root' })
export class AlumnosRoutingResolveService implements Resolve<IAlumnos | null> {
  constructor(protected service: AlumnosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlumnos | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((alumnos: HttpResponse<IAlumnos>) => {
          if (alumnos.body) {
            return of(alumnos.body);
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
