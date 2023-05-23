import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFuncionarios } from '../funcionarios.model';
import { FuncionariosService } from '../service/funcionarios.service';

@Injectable({ providedIn: 'root' })
export class FuncionariosRoutingResolveService implements Resolve<IFuncionarios | null> {
  constructor(protected service: FuncionariosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFuncionarios | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((funcionarios: HttpResponse<IFuncionarios>) => {
          if (funcionarios.body) {
            return of(funcionarios.body);
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
