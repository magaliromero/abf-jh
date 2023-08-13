import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISucursales, NewSucursales } from '../sucursales.model';

export type PartialUpdateSucursales = Partial<ISucursales> & Pick<ISucursales, 'id'>;

export type EntityResponseType = HttpResponse<ISucursales>;
export type EntityArrayResponseType = HttpResponse<ISucursales[]>;

@Injectable({ providedIn: 'root' })
export class SucursalesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sucursales');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sucursales: NewSucursales): Observable<EntityResponseType> {
    return this.http.post<ISucursales>(this.resourceUrl, sucursales, { observe: 'response' });
  }

  update(sucursales: ISucursales): Observable<EntityResponseType> {
    return this.http.put<ISucursales>(`${this.resourceUrl}/${this.getSucursalesIdentifier(sucursales)}`, sucursales, {
      observe: 'response',
    });
  }

  partialUpdate(sucursales: PartialUpdateSucursales): Observable<EntityResponseType> {
    return this.http.patch<ISucursales>(`${this.resourceUrl}/${this.getSucursalesIdentifier(sucursales)}`, sucursales, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISucursales>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISucursales[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSucursalesIdentifier(sucursales: Pick<ISucursales, 'id'>): number {
    return sucursales.id;
  }

  compareSucursales(o1: Pick<ISucursales, 'id'> | null, o2: Pick<ISucursales, 'id'> | null): boolean {
    return o1 && o2 ? this.getSucursalesIdentifier(o1) === this.getSucursalesIdentifier(o2) : o1 === o2;
  }

  addSucursalesToCollectionIfMissing<Type extends Pick<ISucursales, 'id'>>(
    sucursalesCollection: Type[],
    ...sucursalesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sucursales: Type[] = sucursalesToCheck.filter(isPresent);
    if (sucursales.length > 0) {
      const sucursalesCollectionIdentifiers = sucursalesCollection.map(sucursalesItem => this.getSucursalesIdentifier(sucursalesItem)!);
      const sucursalesToAdd = sucursales.filter(sucursalesItem => {
        const sucursalesIdentifier = this.getSucursalesIdentifier(sucursalesItem);
        if (sucursalesCollectionIdentifiers.includes(sucursalesIdentifier)) {
          return false;
        }
        sucursalesCollectionIdentifiers.push(sucursalesIdentifier);
        return true;
      });
      return [...sucursalesToAdd, ...sucursalesCollection];
    }
    return sucursalesCollection;
  }
}
