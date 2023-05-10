import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICobros, NewCobros } from '../cobros.model';

export type PartialUpdateCobros = Partial<ICobros> & Pick<ICobros, 'id'>;

type RestOf<T extends ICobros | NewCobros> = Omit<T, 'fechaRegistro' | 'fechaPago'> & {
  fechaRegistro?: string | null;
  fechaPago?: string | null;
};

export type RestCobros = RestOf<ICobros>;

export type NewRestCobros = RestOf<NewCobros>;

export type PartialUpdateRestCobros = RestOf<PartialUpdateCobros>;

export type EntityResponseType = HttpResponse<ICobros>;
export type EntityArrayResponseType = HttpResponse<ICobros[]>;

@Injectable({ providedIn: 'root' })
export class CobrosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cobros');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cobros: NewCobros): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cobros);
    return this.http
      .post<RestCobros>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cobros: ICobros): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cobros);
    return this.http
      .put<RestCobros>(`${this.resourceUrl}/${this.getCobrosIdentifier(cobros)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cobros: PartialUpdateCobros): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cobros);
    return this.http
      .patch<RestCobros>(`${this.resourceUrl}/${this.getCobrosIdentifier(cobros)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCobros>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCobros[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCobrosIdentifier(cobros: Pick<ICobros, 'id'>): number {
    return cobros.id;
  }

  compareCobros(o1: Pick<ICobros, 'id'> | null, o2: Pick<ICobros, 'id'> | null): boolean {
    return o1 && o2 ? this.getCobrosIdentifier(o1) === this.getCobrosIdentifier(o2) : o1 === o2;
  }

  addCobrosToCollectionIfMissing<Type extends Pick<ICobros, 'id'>>(
    cobrosCollection: Type[],
    ...cobrosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cobros: Type[] = cobrosToCheck.filter(isPresent);
    if (cobros.length > 0) {
      const cobrosCollectionIdentifiers = cobrosCollection.map(cobrosItem => this.getCobrosIdentifier(cobrosItem)!);
      const cobrosToAdd = cobros.filter(cobrosItem => {
        const cobrosIdentifier = this.getCobrosIdentifier(cobrosItem);
        if (cobrosCollectionIdentifiers.includes(cobrosIdentifier)) {
          return false;
        }
        cobrosCollectionIdentifiers.push(cobrosIdentifier);
        return true;
      });
      return [...cobrosToAdd, ...cobrosCollection];
    }
    return cobrosCollection;
  }

  protected convertDateFromClient<T extends ICobros | NewCobros | PartialUpdateCobros>(cobros: T): RestOf<T> {
    return {
      ...cobros,
      fechaRegistro: cobros.fechaRegistro?.format(DATE_FORMAT) ?? null,
      fechaPago: cobros.fechaPago?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCobros: RestCobros): ICobros {
    return {
      ...restCobros,
      fechaRegistro: restCobros.fechaRegistro ? dayjs(restCobros.fechaRegistro) : undefined,
      fechaPago: restCobros.fechaPago ? dayjs(restCobros.fechaPago) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCobros>): HttpResponse<ICobros> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCobros[]>): HttpResponse<ICobros[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
