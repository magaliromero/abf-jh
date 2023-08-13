import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITimbrados, NewTimbrados } from '../timbrados.model';

export type PartialUpdateTimbrados = Partial<ITimbrados> & Pick<ITimbrados, 'id'>;

type RestOf<T extends ITimbrados | NewTimbrados> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

export type RestTimbrados = RestOf<ITimbrados>;

export type NewRestTimbrados = RestOf<NewTimbrados>;

export type PartialUpdateRestTimbrados = RestOf<PartialUpdateTimbrados>;

export type EntityResponseType = HttpResponse<ITimbrados>;
export type EntityArrayResponseType = HttpResponse<ITimbrados[]>;

@Injectable({ providedIn: 'root' })
export class TimbradosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/timbrados');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(timbrados: NewTimbrados): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timbrados);
    return this.http
      .post<RestTimbrados>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(timbrados: ITimbrados): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timbrados);
    return this.http
      .put<RestTimbrados>(`${this.resourceUrl}/${this.getTimbradosIdentifier(timbrados)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(timbrados: PartialUpdateTimbrados): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timbrados);
    return this.http
      .patch<RestTimbrados>(`${this.resourceUrl}/${this.getTimbradosIdentifier(timbrados)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTimbrados>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTimbrados[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTimbradosIdentifier(timbrados: Pick<ITimbrados, 'id'>): number {
    return timbrados.id;
  }

  compareTimbrados(o1: Pick<ITimbrados, 'id'> | null, o2: Pick<ITimbrados, 'id'> | null): boolean {
    return o1 && o2 ? this.getTimbradosIdentifier(o1) === this.getTimbradosIdentifier(o2) : o1 === o2;
  }

  addTimbradosToCollectionIfMissing<Type extends Pick<ITimbrados, 'id'>>(
    timbradosCollection: Type[],
    ...timbradosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const timbrados: Type[] = timbradosToCheck.filter(isPresent);
    if (timbrados.length > 0) {
      const timbradosCollectionIdentifiers = timbradosCollection.map(timbradosItem => this.getTimbradosIdentifier(timbradosItem)!);
      const timbradosToAdd = timbrados.filter(timbradosItem => {
        const timbradosIdentifier = this.getTimbradosIdentifier(timbradosItem);
        if (timbradosCollectionIdentifiers.includes(timbradosIdentifier)) {
          return false;
        }
        timbradosCollectionIdentifiers.push(timbradosIdentifier);
        return true;
      });
      return [...timbradosToAdd, ...timbradosCollection];
    }
    return timbradosCollection;
  }

  protected convertDateFromClient<T extends ITimbrados | NewTimbrados | PartialUpdateTimbrados>(timbrados: T): RestOf<T> {
    return {
      ...timbrados,
      fechaInicio: timbrados.fechaInicio?.format(DATE_FORMAT) ?? null,
      fechaFin: timbrados.fechaFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTimbrados: RestTimbrados): ITimbrados {
    return {
      ...restTimbrados,
      fechaInicio: restTimbrados.fechaInicio ? dayjs(restTimbrados.fechaInicio) : undefined,
      fechaFin: restTimbrados.fechaFin ? dayjs(restTimbrados.fechaFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTimbrados>): HttpResponse<ITimbrados> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTimbrados[]>): HttpResponse<ITimbrados[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
