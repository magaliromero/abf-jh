import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvaluaciones, NewEvaluaciones } from '../evaluaciones.model';

export type PartialUpdateEvaluaciones = Partial<IEvaluaciones> & Pick<IEvaluaciones, 'id'>;

type RestOf<T extends IEvaluaciones | NewEvaluaciones> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestEvaluaciones = RestOf<IEvaluaciones>;

export type NewRestEvaluaciones = RestOf<NewEvaluaciones>;

export type PartialUpdateRestEvaluaciones = RestOf<PartialUpdateEvaluaciones>;

export type EntityResponseType = HttpResponse<IEvaluaciones>;
export type EntityArrayResponseType = HttpResponse<IEvaluaciones[]>;

@Injectable({ providedIn: 'root' })
export class EvaluacionesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evaluaciones');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(evaluaciones: NewEvaluaciones): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluaciones);
    return this.http
      .post<RestEvaluaciones>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(evaluaciones: IEvaluaciones): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluaciones);
    return this.http
      .put<RestEvaluaciones>(`${this.resourceUrl}/${this.getEvaluacionesIdentifier(evaluaciones)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(evaluaciones: PartialUpdateEvaluaciones): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluaciones);
    return this.http
      .patch<RestEvaluaciones>(`${this.resourceUrl}/${this.getEvaluacionesIdentifier(evaluaciones)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEvaluaciones>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEvaluaciones[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEvaluacionesIdentifier(evaluaciones: Pick<IEvaluaciones, 'id'>): number {
    return evaluaciones.id;
  }

  compareEvaluaciones(o1: Pick<IEvaluaciones, 'id'> | null, o2: Pick<IEvaluaciones, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvaluacionesIdentifier(o1) === this.getEvaluacionesIdentifier(o2) : o1 === o2;
  }

  addEvaluacionesToCollectionIfMissing<Type extends Pick<IEvaluaciones, 'id'>>(
    evaluacionesCollection: Type[],
    ...evaluacionesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evaluaciones: Type[] = evaluacionesToCheck.filter(isPresent);
    if (evaluaciones.length > 0) {
      const evaluacionesCollectionIdentifiers = evaluacionesCollection.map(
        evaluacionesItem => this.getEvaluacionesIdentifier(evaluacionesItem)!
      );
      const evaluacionesToAdd = evaluaciones.filter(evaluacionesItem => {
        const evaluacionesIdentifier = this.getEvaluacionesIdentifier(evaluacionesItem);
        if (evaluacionesCollectionIdentifiers.includes(evaluacionesIdentifier)) {
          return false;
        }
        evaluacionesCollectionIdentifiers.push(evaluacionesIdentifier);
        return true;
      });
      return [...evaluacionesToAdd, ...evaluacionesCollection];
    }
    return evaluacionesCollection;
  }

  protected convertDateFromClient<T extends IEvaluaciones | NewEvaluaciones | PartialUpdateEvaluaciones>(evaluaciones: T): RestOf<T> {
    return {
      ...evaluaciones,
      fecha: evaluaciones.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEvaluaciones: RestEvaluaciones): IEvaluaciones {
    return {
      ...restEvaluaciones,
      fecha: restEvaluaciones.fecha ? dayjs(restEvaluaciones.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEvaluaciones>): HttpResponse<IEvaluaciones> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEvaluaciones[]>): HttpResponse<IEvaluaciones[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
