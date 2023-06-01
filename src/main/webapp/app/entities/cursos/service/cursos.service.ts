import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICursos, NewCursos } from '../cursos.model';

export type PartialUpdateCursos = Partial<ICursos> & Pick<ICursos, 'id'>;

type RestOf<T extends ICursos | NewCursos> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

export type RestCursos = RestOf<ICursos>;

export type NewRestCursos = RestOf<NewCursos>;

export type PartialUpdateRestCursos = RestOf<PartialUpdateCursos>;

export type EntityResponseType = HttpResponse<ICursos>;
export type EntityArrayResponseType = HttpResponse<ICursos[]>;

@Injectable({ providedIn: 'root' })
export class CursosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cursos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cursos: NewCursos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cursos);
    return this.http
      .post<RestCursos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cursos: ICursos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cursos);
    return this.http
      .put<RestCursos>(`${this.resourceUrl}/${this.getCursosIdentifier(cursos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cursos: PartialUpdateCursos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cursos);
    return this.http
      .patch<RestCursos>(`${this.resourceUrl}/${this.getCursosIdentifier(cursos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCursos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCursos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCursosIdentifier(cursos: Pick<ICursos, 'id'>): number {
    return cursos.id;
  }

  compareCursos(o1: Pick<ICursos, 'id'> | null, o2: Pick<ICursos, 'id'> | null): boolean {
    return o1 && o2 ? this.getCursosIdentifier(o1) === this.getCursosIdentifier(o2) : o1 === o2;
  }

  addCursosToCollectionIfMissing<Type extends Pick<ICursos, 'id'>>(
    cursosCollection: Type[],
    ...cursosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cursos: Type[] = cursosToCheck.filter(isPresent);
    if (cursos.length > 0) {
      const cursosCollectionIdentifiers = cursosCollection.map(cursosItem => this.getCursosIdentifier(cursosItem)!);
      const cursosToAdd = cursos.filter(cursosItem => {
        const cursosIdentifier = this.getCursosIdentifier(cursosItem);
        if (cursosCollectionIdentifiers.includes(cursosIdentifier)) {
          return false;
        }
        cursosCollectionIdentifiers.push(cursosIdentifier);
        return true;
      });
      return [...cursosToAdd, ...cursosCollection];
    }
    return cursosCollection;
  }

  protected convertDateFromClient<T extends ICursos | NewCursos | PartialUpdateCursos>(cursos: T): RestOf<T> {
    return {
      ...cursos,
      fechaInicio: cursos.fechaInicio?.format(DATE_FORMAT) ?? null,
      fechaFin: cursos.fechaFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCursos: RestCursos): ICursos {
    return {
      ...restCursos,
      fechaInicio: restCursos.fechaInicio ? dayjs(restCursos.fechaInicio) : undefined,
      fechaFin: restCursos.fechaFin ? dayjs(restCursos.fechaFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCursos>): HttpResponse<ICursos> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCursos[]>): HttpResponse<ICursos[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
