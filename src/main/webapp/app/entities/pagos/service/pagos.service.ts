import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPagos, NewPagos } from '../pagos.model';

export type PartialUpdatePagos = Partial<IPagos> & Pick<IPagos, 'id'>;

type RestOf<T extends IPagos | NewPagos> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestPagos = RestOf<IPagos>;

export type NewRestPagos = RestOf<NewPagos>;

export type PartialUpdateRestPagos = RestOf<PartialUpdatePagos>;

export type EntityResponseType = HttpResponse<IPagos>;
export type EntityArrayResponseType = HttpResponse<IPagos[]>;

@Injectable({ providedIn: 'root' })
export class PagosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pagos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pagos: NewPagos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pagos);
    return this.http.post<RestPagos>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pagos: IPagos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pagos);
    return this.http
      .put<RestPagos>(`${this.resourceUrl}/${this.getPagosIdentifier(pagos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pagos: PartialUpdatePagos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pagos);
    return this.http
      .patch<RestPagos>(`${this.resourceUrl}/${this.getPagosIdentifier(pagos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPagos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPagos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPagosIdentifier(pagos: Pick<IPagos, 'id'>): number {
    return pagos.id;
  }

  comparePagos(o1: Pick<IPagos, 'id'> | null, o2: Pick<IPagos, 'id'> | null): boolean {
    return o1 && o2 ? this.getPagosIdentifier(o1) === this.getPagosIdentifier(o2) : o1 === o2;
  }

  addPagosToCollectionIfMissing<Type extends Pick<IPagos, 'id'>>(
    pagosCollection: Type[],
    ...pagosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pagos: Type[] = pagosToCheck.filter(isPresent);
    if (pagos.length > 0) {
      const pagosCollectionIdentifiers = pagosCollection.map(pagosItem => this.getPagosIdentifier(pagosItem)!);
      const pagosToAdd = pagos.filter(pagosItem => {
        const pagosIdentifier = this.getPagosIdentifier(pagosItem);
        if (pagosCollectionIdentifiers.includes(pagosIdentifier)) {
          return false;
        }
        pagosCollectionIdentifiers.push(pagosIdentifier);
        return true;
      });
      return [...pagosToAdd, ...pagosCollection];
    }
    return pagosCollection;
  }

  protected convertDateFromClient<T extends IPagos | NewPagos | PartialUpdatePagos>(pagos: T): RestOf<T> {
    return {
      ...pagos,
      fecha: pagos.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPagos: RestPagos): IPagos {
    return {
      ...restPagos,
      fecha: restPagos.fecha ? dayjs(restPagos.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPagos>): HttpResponse<IPagos> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPagos[]>): HttpResponse<IPagos[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
