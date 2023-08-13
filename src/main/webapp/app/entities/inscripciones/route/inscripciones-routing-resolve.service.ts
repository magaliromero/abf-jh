import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInscripciones } from '../inscripciones.model';
import { InscripcionesService } from '../service/inscripciones.service';

@Injectable({ providedIn: 'root' })
export class InscripcionesRoutingResolveService implements Resolve<IInscripciones | null> {
  constructor(protected service: InscripcionesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInscripciones | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inscripciones: HttpResponse<IInscripciones>) => {
          if (inscripciones.body) {
            return of(inscripciones.body);
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
