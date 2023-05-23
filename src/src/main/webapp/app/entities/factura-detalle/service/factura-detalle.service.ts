import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFacturaDetalle, NewFacturaDetalle } from '../factura-detalle.model';

export type PartialUpdateFacturaDetalle = Partial<IFacturaDetalle> & Pick<IFacturaDetalle, 'id'>;

export type EntityResponseType = HttpResponse<IFacturaDetalle>;
export type EntityArrayResponseType = HttpResponse<IFacturaDetalle[]>;

@Injectable({ providedIn: 'root' })
export class FacturaDetalleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/factura-detalles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(facturaDetalle: NewFacturaDetalle): Observable<EntityResponseType> {
    return this.http.post<IFacturaDetalle>(this.resourceUrl, facturaDetalle, { observe: 'response' });
  }

  update(facturaDetalle: IFacturaDetalle): Observable<EntityResponseType> {
    return this.http.put<IFacturaDetalle>(`${this.resourceUrl}/${this.getFacturaDetalleIdentifier(facturaDetalle)}`, facturaDetalle, {
      observe: 'response',
    });
  }

  partialUpdate(facturaDetalle: PartialUpdateFacturaDetalle): Observable<EntityResponseType> {
    return this.http.patch<IFacturaDetalle>(`${this.resourceUrl}/${this.getFacturaDetalleIdentifier(facturaDetalle)}`, facturaDetalle, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFacturaDetalle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFacturaDetalle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFacturaDetalleIdentifier(facturaDetalle: Pick<IFacturaDetalle, 'id'>): number {
    return facturaDetalle.id;
  }

  compareFacturaDetalle(o1: Pick<IFacturaDetalle, 'id'> | null, o2: Pick<IFacturaDetalle, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacturaDetalleIdentifier(o1) === this.getFacturaDetalleIdentifier(o2) : o1 === o2;
  }

  addFacturaDetalleToCollectionIfMissing<Type extends Pick<IFacturaDetalle, 'id'>>(
    facturaDetalleCollection: Type[],
    ...facturaDetallesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facturaDetalles: Type[] = facturaDetallesToCheck.filter(isPresent);
    if (facturaDetalles.length > 0) {
      const facturaDetalleCollectionIdentifiers = facturaDetalleCollection.map(
        facturaDetalleItem => this.getFacturaDetalleIdentifier(facturaDetalleItem)!
      );
      const facturaDetallesToAdd = facturaDetalles.filter(facturaDetalleItem => {
        const facturaDetalleIdentifier = this.getFacturaDetalleIdentifier(facturaDetalleItem);
        if (facturaDetalleCollectionIdentifiers.includes(facturaDetalleIdentifier)) {
          return false;
        }
        facturaDetalleCollectionIdentifiers.push(facturaDetalleIdentifier);
        return true;
      });
      return [...facturaDetallesToAdd, ...facturaDetalleCollection];
    }
    return facturaDetalleCollection;
  }
}
