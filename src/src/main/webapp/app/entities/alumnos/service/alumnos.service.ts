import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlumnos, NewAlumnos } from '../alumnos.model';

export type PartialUpdateAlumnos = Partial<IAlumnos> & Pick<IAlumnos, 'id'>;

type RestOf<T extends IAlumnos | NewAlumnos> = Omit<T, 'fechaNacimiento'> & {
  fechaNacimiento?: string | null;
};

export type RestAlumnos = RestOf<IAlumnos>;

export type NewRestAlumnos = RestOf<NewAlumnos>;

export type PartialUpdateRestAlumnos = RestOf<PartialUpdateAlumnos>;

export type EntityResponseType = HttpResponse<IAlumnos>;
export type EntityArrayResponseType = HttpResponse<IAlumnos[]>;

@Injectable({ providedIn: 'root' })
export class AlumnosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alumnos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(alumnos: NewAlumnos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alumnos);
    return this.http
      .post<RestAlumnos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alumnos: IAlumnos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alumnos);
    return this.http
      .put<RestAlumnos>(`${this.resourceUrl}/${this.getAlumnosIdentifier(alumnos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alumnos: PartialUpdateAlumnos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alumnos);
    return this.http
      .patch<RestAlumnos>(`${this.resourceUrl}/${this.getAlumnosIdentifier(alumnos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlumnos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlumnos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlumnosIdentifier(alumnos: Pick<IAlumnos, 'id'>): number {
    return alumnos.id;
  }

  compareAlumnos(o1: Pick<IAlumnos, 'id'> | null, o2: Pick<IAlumnos, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlumnosIdentifier(o1) === this.getAlumnosIdentifier(o2) : o1 === o2;
  }

  addAlumnosToCollectionIfMissing<Type extends Pick<IAlumnos, 'id'>>(
    alumnosCollection: Type[],
    ...alumnosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alumnos: Type[] = alumnosToCheck.filter(isPresent);
    if (alumnos.length > 0) {
      const alumnosCollectionIdentifiers = alumnosCollection.map(alumnosItem => this.getAlumnosIdentifier(alumnosItem)!);
      const alumnosToAdd = alumnos.filter(alumnosItem => {
        const alumnosIdentifier = this.getAlumnosIdentifier(alumnosItem);
        if (alumnosCollectionIdentifiers.includes(alumnosIdentifier)) {
          return false;
        }
        alumnosCollectionIdentifiers.push(alumnosIdentifier);
        return true;
      });
      return [...alumnosToAdd, ...alumnosCollection];
    }
    return alumnosCollection;
  }

  protected convertDateFromClient<T extends IAlumnos | NewAlumnos | PartialUpdateAlumnos>(alumnos: T): RestOf<T> {
    return {
      ...alumnos,
      fechaNacimiento: alumnos.fechaNacimiento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAlumnos: RestAlumnos): IAlumnos {
    return {
      ...restAlumnos,
      fechaNacimiento: restAlumnos.fechaNacimiento ? dayjs(restAlumnos.fechaNacimiento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlumnos>): HttpResponse<IAlumnos> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlumnos[]>): HttpResponse<IAlumnos[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
