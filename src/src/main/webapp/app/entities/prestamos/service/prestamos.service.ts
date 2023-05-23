import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrestamos, NewPrestamos } from '../prestamos.model';

export type PartialUpdatePrestamos = Partial<IPrestamos> & Pick<IPrestamos, 'id'>;

type RestOf<T extends IPrestamos | NewPrestamos> = Omit<T, 'fechaPrestamo' | 'fechaDevolucion'> & {
  fechaPrestamo?: string | null;
  fechaDevolucion?: string | null;
};

export type RestPrestamos = RestOf<IPrestamos>;

export type NewRestPrestamos = RestOf<NewPrestamos>;

export type PartialUpdateRestPrestamos = RestOf<PartialUpdatePrestamos>;

export type EntityResponseType = HttpResponse<IPrestamos>;
export type EntityArrayResponseType = HttpResponse<IPrestamos[]>;

@Injectable({ providedIn: 'root' })
export class PrestamosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prestamos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prestamos: NewPrestamos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestamos);
    return this.http
      .post<RestPrestamos>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(prestamos: IPrestamos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestamos);
    return this.http
      .put<RestPrestamos>(`${this.resourceUrl}/${this.getPrestamosIdentifier(prestamos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(prestamos: PartialUpdatePrestamos): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestamos);
    return this.http
      .patch<RestPrestamos>(`${this.resourceUrl}/${this.getPrestamosIdentifier(prestamos)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPrestamos>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPrestamos[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPrestamosIdentifier(prestamos: Pick<IPrestamos, 'id'>): number {
    return prestamos.id;
  }

  comparePrestamos(o1: Pick<IPrestamos, 'id'> | null, o2: Pick<IPrestamos, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrestamosIdentifier(o1) === this.getPrestamosIdentifier(o2) : o1 === o2;
  }

  addPrestamosToCollectionIfMissing<Type extends Pick<IPrestamos, 'id'>>(
    prestamosCollection: Type[],
    ...prestamosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prestamos: Type[] = prestamosToCheck.filter(isPresent);
    if (prestamos.length > 0) {
      const prestamosCollectionIdentifiers = prestamosCollection.map(prestamosItem => this.getPrestamosIdentifier(prestamosItem)!);
      const prestamosToAdd = prestamos.filter(prestamosItem => {
        const prestamosIdentifier = this.getPrestamosIdentifier(prestamosItem);
        if (prestamosCollectionIdentifiers.includes(prestamosIdentifier)) {
          return false;
        }
        prestamosCollectionIdentifiers.push(prestamosIdentifier);
        return true;
      });
      return [...prestamosToAdd, ...prestamosCollection];
    }
    return prestamosCollection;
  }

  protected convertDateFromClient<T extends IPrestamos | NewPrestamos | PartialUpdatePrestamos>(prestamos: T): RestOf<T> {
    return {
      ...prestamos,
      fechaPrestamo: prestamos.fechaPrestamo?.format(DATE_FORMAT) ?? null,
      fechaDevolucion: prestamos.fechaDevolucion?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPrestamos: RestPrestamos): IPrestamos {
    return {
      ...restPrestamos,
      fechaPrestamo: restPrestamos.fechaPrestamo ? dayjs(restPrestamos.fechaPrestamo) : undefined,
      fechaDevolucion: restPrestamos.fechaDevolucion ? dayjs(restPrestamos.fechaDevolucion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPrestamos>): HttpResponse<IPrestamos> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPrestamos[]>): HttpResponse<IPrestamos[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
