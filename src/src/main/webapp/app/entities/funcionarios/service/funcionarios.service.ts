import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFuncionarios, NewFuncionarios } from '../funcionarios.model';

export type PartialUpdateFuncionarios = Partial<IFuncionarios> & Pick<IFuncionarios, 'id'>;

type RestOf<T extends IFuncionarios | NewFuncionarios> = Omit<T, 'fechaNacimiento'> & {
  fechaNacimiento?: string | null;
};

export type RestFuncionarios = RestOf<IFuncionarios>;

export type NewRestFuncionarios = RestOf<NewFuncionarios>;

export type PartialUpdateRestFuncionarios = RestOf<PartialUpdateFuncionarios>;

export type EntityResponseType = HttpResponse<IFuncionarios>;
export type EntityArrayResponseType = HttpResponse<IFuncionarios[]>;

@Injectable({ providedIn: 'root' })
export class FuncionariosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/funcionarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(funcionarios: NewFuncionarios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionarios);
    return this.http
      .post<RestFuncionarios>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(funcionarios: IFuncionarios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionarios);
    return this.http
      .put<RestFuncionarios>(`${this.resourceUrl}/${this.getFuncionariosIdentifier(funcionarios)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(funcionarios: PartialUpdateFuncionarios): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionarios);
    return this.http
      .patch<RestFuncionarios>(`${this.resourceUrl}/${this.getFuncionariosIdentifier(funcionarios)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFuncionarios>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFuncionarios[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFuncionariosIdentifier(funcionarios: Pick<IFuncionarios, 'id'>): number {
    return funcionarios.id;
  }

  compareFuncionarios(o1: Pick<IFuncionarios, 'id'> | null, o2: Pick<IFuncionarios, 'id'> | null): boolean {
    return o1 && o2 ? this.getFuncionariosIdentifier(o1) === this.getFuncionariosIdentifier(o2) : o1 === o2;
  }

  addFuncionariosToCollectionIfMissing<Type extends Pick<IFuncionarios, 'id'>>(
    funcionariosCollection: Type[],
    ...funcionariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const funcionarios: Type[] = funcionariosToCheck.filter(isPresent);
    if (funcionarios.length > 0) {
      const funcionariosCollectionIdentifiers = funcionariosCollection.map(
        funcionariosItem => this.getFuncionariosIdentifier(funcionariosItem)!
      );
      const funcionariosToAdd = funcionarios.filter(funcionariosItem => {
        const funcionariosIdentifier = this.getFuncionariosIdentifier(funcionariosItem);
        if (funcionariosCollectionIdentifiers.includes(funcionariosIdentifier)) {
          return false;
        }
        funcionariosCollectionIdentifiers.push(funcionariosIdentifier);
        return true;
      });
      return [...funcionariosToAdd, ...funcionariosCollection];
    }
    return funcionariosCollection;
  }

  protected convertDateFromClient<T extends IFuncionarios | NewFuncionarios | PartialUpdateFuncionarios>(funcionarios: T): RestOf<T> {
    return {
      ...funcionarios,
      fechaNacimiento: funcionarios.fechaNacimiento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFuncionarios: RestFuncionarios): IFuncionarios {
    return {
      ...restFuncionarios,
      fechaNacimiento: restFuncionarios.fechaNacimiento ? dayjs(restFuncionarios.fechaNacimiento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFuncionarios>): HttpResponse<IFuncionarios> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFuncionarios[]>): HttpResponse<IFuncionarios[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
