import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFacturas, NewFacturas } from '../facturas.model';

export type PartialUpdateFacturas = Partial<IFacturas> & Pick<IFacturas, 'id'>;

type RestOf<T extends IFacturas | NewFacturas> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestFacturas = RestOf<IFacturas>;

export type NewRestFacturas = RestOf<NewFacturas>;

export type PartialUpdateRestFacturas = RestOf<PartialUpdateFacturas>;

export type EntityResponseType = HttpResponse<IFacturas>;
export type EntityArrayResponseType = HttpResponse<IFacturas[]>;

@Injectable({ providedIn: 'root' })
export class FacturasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/facturas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(facturas: NewFacturas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturas);
    return this.http
      .post<RestFacturas>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(facturas: IFacturas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturas);
    return this.http
      .put<RestFacturas>(`${this.resourceUrl}/${this.getFacturasIdentifier(facturas)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(facturas: PartialUpdateFacturas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(facturas);
    return this.http
      .patch<RestFacturas>(`${this.resourceUrl}/${this.getFacturasIdentifier(facturas)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFacturas>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFacturas[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFacturasIdentifier(facturas: Pick<IFacturas, 'id'>): number {
    return facturas.id;
  }

  compareFacturas(o1: Pick<IFacturas, 'id'> | null, o2: Pick<IFacturas, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacturasIdentifier(o1) === this.getFacturasIdentifier(o2) : o1 === o2;
  }

  addFacturasToCollectionIfMissing<Type extends Pick<IFacturas, 'id'>>(
    facturasCollection: Type[],
    ...facturasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facturas: Type[] = facturasToCheck.filter(isPresent);
    if (facturas.length > 0) {
      const facturasCollectionIdentifiers = facturasCollection.map(facturasItem => this.getFacturasIdentifier(facturasItem)!);
      const facturasToAdd = facturas.filter(facturasItem => {
        const facturasIdentifier = this.getFacturasIdentifier(facturasItem);
        if (facturasCollectionIdentifiers.includes(facturasIdentifier)) {
          return false;
        }
        facturasCollectionIdentifiers.push(facturasIdentifier);
        return true;
      });
      return [...facturasToAdd, ...facturasCollection];
    }
    return facturasCollection;
  }

  protected convertDateFromClient<T extends IFacturas | NewFacturas | PartialUpdateFacturas>(facturas: T): RestOf<T> {
    return {
      ...facturas,
      fecha: facturas.fecha?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFacturas: RestFacturas): IFacturas {
    return {
      ...restFacturas,
      fecha: restFacturas.fecha ? dayjs(restFacturas.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFacturas>): HttpResponse<IFacturas> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFacturas[]>): HttpResponse<IFacturas[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
