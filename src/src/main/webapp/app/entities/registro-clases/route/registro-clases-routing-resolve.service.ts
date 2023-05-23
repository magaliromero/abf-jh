import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRegistroClases } from '../registro-clases.model';
import { RegistroClasesService } from '../service/registro-clases.service';

@Injectable({ providedIn: 'root' })
export class RegistroClasesRoutingResolveService implements Resolve<IRegistroClases | null> {
  constructor(protected service: RegistroClasesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegistroClases | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((registroClases: HttpResponse<IRegistroClases>) => {
          if (registroClases.body) {
            return of(registroClases.body);
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
