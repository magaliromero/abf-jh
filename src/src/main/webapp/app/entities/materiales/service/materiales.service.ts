import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMateriales, NewMateriales } from '../materiales.model';

export type PartialUpdateMateriales = Partial<IMateriales> & Pick<IMateriales, 'id'>;

export type EntityResponseType = HttpResponse<IMateriales>;
export type EntityArrayResponseType = HttpResponse<IMateriales[]>;

@Injectable({ providedIn: 'root' })
export class MaterialesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/materiales');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(materiales: NewMateriales): Observable<EntityResponseType> {
    return this.http.post<IMateriales>(this.resourceUrl, materiales, { observe: 'response' });
  }

  update(materiales: IMateriales): Observable<EntityResponseType> {
    return this.http.put<IMateriales>(`${this.resourceUrl}/${this.getMaterialesIdentifier(materiales)}`, materiales, {
      observe: 'response',
    });
  }

  partialUpdate(materiales: PartialUpdateMateriales): Observable<EntityResponseType> {
    return this.http.patch<IMateriales>(`${this.resourceUrl}/${this.getMaterialesIdentifier(materiales)}`, materiales, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMateriales>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMateriales[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMaterialesIdentifier(materiales: Pick<IMateriales, 'id'>): number {
    return materiales.id;
  }

  compareMateriales(o1: Pick<IMateriales, 'id'> | null, o2: Pick<IMateriales, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaterialesIdentifier(o1) === this.getMaterialesIdentifier(o2) : o1 === o2;
  }

  addMaterialesToCollectionIfMissing<Type extends Pick<IMateriales, 'id'>>(
    materialesCollection: Type[],
    ...materialesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const materiales: Type[] = materialesToCheck.filter(isPresent);
    if (materiales.length > 0) {
      const materialesCollectionIdentifiers = materialesCollection.map(materialesItem => this.getMaterialesIdentifier(materialesItem)!);
      const materialesToAdd = materiales.filter(materialesItem => {
        const materialesIdentifier = this.getMaterialesIdentifier(materialesItem);
        if (materialesCollectionIdentifiers.includes(materialesIdentifier)) {
          return false;
        }
        materialesCollectionIdentifiers.push(materialesIdentifier);
        return true;
      });
      return [...materialesToAdd, ...materialesCollection];
    }
    return materialesCollection;
  }
}
