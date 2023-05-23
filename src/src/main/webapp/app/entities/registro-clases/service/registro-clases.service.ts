import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRegistroClases, NewRegistroClases } from '../registro-clases.model';

export type PartialUpdateRegistroClases = Partial<IRegistroClases> & Pick<IRegistroClases, 'id'>;

type RestOf<T extends IRegistroClases | NewRegistroClases> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestRegistroClases = RestOf<IRegistroClases>;

export type NewRestRegistroClases = RestOf<NewRegistroClases>;

export type PartialUpdateRestRegistroClases = RestOf<PartialUpdateRegistroClases>;

export type EntityResponseType = HttpResponse<IRegistroClases>;
export type EntityArrayResponseType = HttpResponse<IRegistroClases[]>;

@Injectable({ providedIn: 'root' })
export class RegistroClasesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/registro-clases');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(registroClases: NewRegistroClases): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroClases);
    return this.http
      .post<RestRegistroClases>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(registroClases: IRegistroClases): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroClases);
    return this.http
      .put<RestRegistroClases>(`${this.resourceUrl}/${this.getRegistroClasesIdentifier(registroClases)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(registroClases: PartialUpdateRegistroClases): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registroClases);
    return this.http
      .patch<RestRegistroClases>(`${this.resourceUrl}/${this.getRegistroClasesIdentifier(registroClases)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRegistroClases>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRegistroClases[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRegistroClasesIdentifier(registroClases: Pick<IRegistroClases, 'id'>): number {
    return registroClases.id;
  }

  compareRegistroClases(o1: Pick<IRegistroClases, 'id'> | null, o2: Pick<IRegistroClases, 'id'> | null): boolean {
    return o1 && o2 ? this.getRegistroClasesIdentifier(o1) === this.getRegistroClasesIdentifier(o2) : o1 === o2;
  }

  addRegistroClasesToCollectionIfMissing<Type extends Pick<IRegistroClases, 'id'>>(
    registroClasesCollection: Type[],
    ...registroClasesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const registroClases: Type[] = registroClasesToCheck.filter(isPresent);
    if (registroClases.length > 0) {
      const registroClasesCollectionIdentifiers = registroClasesCollection.map(
        registroClasesItem => this.getRegistroClasesIdentifier(registroClasesItem)!
      );
      const registroClasesToAdd = registroClases.filter(registroClasesItem => {
        const registroClasesIdentifier = this.getRegistroClasesIdentifier(registroClasesItem);
        if (registroClasesCollectionIdentifiers.includes(registroClasesIdentifier)) {
          return false;
        }
        registroClasesCollectionIdentifiers.push(registroClasesIdentifier);
        return true;
      });
      return [...registroClasesToAdd, ...registroClasesCollection];
    }
    return registroClasesCollection;
  }

  protected convertDateFromClient<T extends IRegistroClases | NewRegistroClases | PartialUpdateRegistroClases>(
    registroClases: T
  ): RestOf<T> {
    return {
      ...registroClases,
      fecha: registroClases.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restRegistroClases: RestRegistroClases): IRegistroClases {
    return {
      ...restRegistroClases,
      fecha: restRegistroClases.fecha ? dayjs(restRegistroClases.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRegistroClases>): HttpResponse<IRegistroClases> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRegistroClases[]>): HttpResponse<IRegistroClases[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
