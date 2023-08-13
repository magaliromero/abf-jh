import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInscripciones, NewInscripciones } from '../inscripciones.model';

export type PartialUpdateInscripciones = Partial<IInscripciones> & Pick<IInscripciones, 'id'>;

type RestOf<T extends IInscripciones | NewInscripciones> = Omit<T, 'fechaInscripcion'> & {
  fechaInscripcion?: string | null;
};

export type RestInscripciones = RestOf<IInscripciones>;

export type NewRestInscripciones = RestOf<NewInscripciones>;

export type PartialUpdateRestInscripciones = RestOf<PartialUpdateInscripciones>;

export type EntityResponseType = HttpResponse<IInscripciones>;
export type EntityArrayResponseType = HttpResponse<IInscripciones[]>;

@Injectable({ providedIn: 'root' })
export class InscripcionesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inscripciones');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inscripciones: NewInscripciones): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscripciones);
    return this.http
      .post<RestInscripciones>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inscripciones: IInscripciones): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscripciones);
    return this.http
      .put<RestInscripciones>(`${this.resourceUrl}/${this.getInscripcionesIdentifier(inscripciones)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inscripciones: PartialUpdateInscripciones): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscripciones);
    return this.http
      .patch<RestInscripciones>(`${this.resourceUrl}/${this.getInscripcionesIdentifier(inscripciones)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInscripciones>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInscripciones[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInscripcionesIdentifier(inscripciones: Pick<IInscripciones, 'id'>): number {
    return inscripciones.id;
  }

  compareInscripciones(o1: Pick<IInscripciones, 'id'> | null, o2: Pick<IInscripciones, 'id'> | null): boolean {
    return o1 && o2 ? this.getInscripcionesIdentifier(o1) === this.getInscripcionesIdentifier(o2) : o1 === o2;
  }

  addInscripcionesToCollectionIfMissing<Type extends Pick<IInscripciones, 'id'>>(
    inscripcionesCollection: Type[],
    ...inscripcionesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inscripciones: Type[] = inscripcionesToCheck.filter(isPresent);
    if (inscripciones.length > 0) {
      const inscripcionesCollectionIdentifiers = inscripcionesCollection.map(
        inscripcionesItem => this.getInscripcionesIdentifier(inscripcionesItem)!
      );
      const inscripcionesToAdd = inscripciones.filter(inscripcionesItem => {
        const inscripcionesIdentifier = this.getInscripcionesIdentifier(inscripcionesItem);
        if (inscripcionesCollectionIdentifiers.includes(inscripcionesIdentifier)) {
          return false;
        }
        inscripcionesCollectionIdentifiers.push(inscripcionesIdentifier);
        return true;
      });
      return [...inscripcionesToAdd, ...inscripcionesCollection];
    }
    return inscripcionesCollection;
  }

  protected convertDateFromClient<T extends IInscripciones | NewInscripciones | PartialUpdateInscripciones>(inscripciones: T): RestOf<T> {
    return {
      ...inscripciones,
      fechaInscripcion: inscripciones.fechaInscripcion?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restInscripciones: RestInscripciones): IInscripciones {
    return {
      ...restInscripciones,
      fechaInscripcion: restInscripciones.fechaInscripcion ? dayjs(restInscripciones.fechaInscripcion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInscripciones>): HttpResponse<IInscripciones> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInscripciones[]>): HttpResponse<IInscripciones[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
