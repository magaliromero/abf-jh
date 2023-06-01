import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClientes, NewClientes } from '../clientes.model';

export type PartialUpdateClientes = Partial<IClientes> & Pick<IClientes, 'id'>;

type RestOf<T extends IClientes | NewClientes> = Omit<T, 'fechaNacimiento'> & {
  fechaNacimiento?: string | null;
};

export type RestClientes = RestOf<IClientes>;

export type NewRestClientes = RestOf<NewClientes>;

export type PartialUpdateRestClientes = RestOf<PartialUpdateClientes>;

export type EntityResponseType = HttpResponse<IClientes>;
export type EntityArrayResponseType = HttpResponse<IClientes[]>;

@Injectable({ providedIn: 'root' })
export class ClientesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clientes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(clientes: NewClientes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clientes);
    return this.http
      .post<RestClientes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(clientes: IClientes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clientes);
    return this.http
      .put<RestClientes>(`${this.resourceUrl}/${this.getClientesIdentifier(clientes)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(clientes: PartialUpdateClientes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(clientes);
    return this.http
      .patch<RestClientes>(`${this.resourceUrl}/${this.getClientesIdentifier(clientes)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestClientes>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestClientes[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClientesIdentifier(clientes: Pick<IClientes, 'id'>): number {
    return clientes.id;
  }

  compareClientes(o1: Pick<IClientes, 'id'> | null, o2: Pick<IClientes, 'id'> | null): boolean {
    return o1 && o2 ? this.getClientesIdentifier(o1) === this.getClientesIdentifier(o2) : o1 === o2;
  }

  addClientesToCollectionIfMissing<Type extends Pick<IClientes, 'id'>>(
    clientesCollection: Type[],
    ...clientesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const clientes: Type[] = clientesToCheck.filter(isPresent);
    if (clientes.length > 0) {
      const clientesCollectionIdentifiers = clientesCollection.map(clientesItem => this.getClientesIdentifier(clientesItem)!);
      const clientesToAdd = clientes.filter(clientesItem => {
        const clientesIdentifier = this.getClientesIdentifier(clientesItem);
        if (clientesCollectionIdentifiers.includes(clientesIdentifier)) {
          return false;
        }
        clientesCollectionIdentifiers.push(clientesIdentifier);
        return true;
      });
      return [...clientesToAdd, ...clientesCollection];
    }
    return clientesCollection;
  }

  protected convertDateFromClient<T extends IClientes | NewClientes | PartialUpdateClientes>(clientes: T): RestOf<T> {
    return {
      ...clientes,
      fechaNacimiento: clientes.fechaNacimiento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restClientes: RestClientes): IClientes {
    return {
      ...restClientes,
      fechaNacimiento: restClientes.fechaNacimiento ? dayjs(restClientes.fechaNacimiento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestClientes>): HttpResponse<IClientes> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestClientes[]>): HttpResponse<IClientes[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
