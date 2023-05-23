import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITiposDocumentos } from '../tipos-documentos.model';
import { TiposDocumentosService } from '../service/tipos-documentos.service';

@Injectable({ providedIn: 'root' })
export class TiposDocumentosRoutingResolveService implements Resolve<ITiposDocumentos | null> {
  constructor(protected service: TiposDocumentosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITiposDocumentos | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tiposDocumentos: HttpResponse<ITiposDocumentos>) => {
          if (tiposDocumentos.body) {
            return of(tiposDocumentos.body);
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
