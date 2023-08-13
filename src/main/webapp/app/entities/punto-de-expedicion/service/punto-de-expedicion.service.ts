import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPuntoDeExpedicion, NewPuntoDeExpedicion } from '../punto-de-expedicion.model';

export type PartialUpdatePuntoDeExpedicion = Partial<IPuntoDeExpedicion> & Pick<IPuntoDeExpedicion, 'id'>;

export type EntityResponseType = HttpResponse<IPuntoDeExpedicion>;
export type EntityArrayResponseType = HttpResponse<IPuntoDeExpedicion[]>;

@Injectable({ providedIn: 'root' })
export class PuntoDeExpedicionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/punto-de-expedicions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(puntoDeExpedicion: NewPuntoDeExpedicion): Observable<EntityResponseType> {
    return this.http.post<IPuntoDeExpedicion>(this.resourceUrl, puntoDeExpedicion, { observe: 'response' });
  }

  update(puntoDeExpedicion: IPuntoDeExpedicion): Observable<EntityResponseType> {
    return this.http.put<IPuntoDeExpedicion>(
      `${this.resourceUrl}/${this.getPuntoDeExpedicionIdentifier(puntoDeExpedicion)}`,
      puntoDeExpedicion,
      { observe: 'response' }
    );
  }

  partialUpdate(puntoDeExpedicion: PartialUpdatePuntoDeExpedicion): Observable<EntityResponseType> {
    return this.http.patch<IPuntoDeExpedicion>(
      `${this.resourceUrl}/${this.getPuntoDeExpedicionIdentifier(puntoDeExpedicion)}`,
      puntoDeExpedicion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPuntoDeExpedicion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPuntoDeExpedicion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPuntoDeExpedicionIdentifier(puntoDeExpedicion: Pick<IPuntoDeExpedicion, 'id'>): number {
    return puntoDeExpedicion.id;
  }

  comparePuntoDeExpedicion(o1: Pick<IPuntoDeExpedicion, 'id'> | null, o2: Pick<IPuntoDeExpedicion, 'id'> | null): boolean {
    return o1 && o2 ? this.getPuntoDeExpedicionIdentifier(o1) === this.getPuntoDeExpedicionIdentifier(o2) : o1 === o2;
  }

  addPuntoDeExpedicionToCollectionIfMissing<Type extends Pick<IPuntoDeExpedicion, 'id'>>(
    puntoDeExpedicionCollection: Type[],
    ...puntoDeExpedicionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const puntoDeExpedicions: Type[] = puntoDeExpedicionsToCheck.filter(isPresent);
    if (puntoDeExpedicions.length > 0) {
      const puntoDeExpedicionCollectionIdentifiers = puntoDeExpedicionCollection.map(
        puntoDeExpedicionItem => this.getPuntoDeExpedicionIdentifier(puntoDeExpedicionItem)!
      );
      const puntoDeExpedicionsToAdd = puntoDeExpedicions.filter(puntoDeExpedicionItem => {
        const puntoDeExpedicionIdentifier = this.getPuntoDeExpedicionIdentifier(puntoDeExpedicionItem);
        if (puntoDeExpedicionCollectionIdentifiers.includes(puntoDeExpedicionIdentifier)) {
          return false;
        }
        puntoDeExpedicionCollectionIdentifiers.push(puntoDeExpedicionIdentifier);
        return true;
      });
      return [...puntoDeExpedicionsToAdd, ...puntoDeExpedicionCollection];
    }
    return puntoDeExpedicionCollection;
  }
}
