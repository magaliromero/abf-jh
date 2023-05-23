import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITemas, NewTemas } from '../temas.model';

export type PartialUpdateTemas = Partial<ITemas> & Pick<ITemas, 'id'>;

export type EntityResponseType = HttpResponse<ITemas>;
export type EntityArrayResponseType = HttpResponse<ITemas[]>;

@Injectable({ providedIn: 'root' })
export class TemasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/temas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(temas: NewTemas): Observable<EntityResponseType> {
    return this.http.post<ITemas>(this.resourceUrl, temas, { observe: 'response' });
  }

  update(temas: ITemas): Observable<EntityResponseType> {
    return this.http.put<ITemas>(`${this.resourceUrl}/${this.getTemasIdentifier(temas)}`, temas, { observe: 'response' });
  }

  partialUpdate(temas: PartialUpdateTemas): Observable<EntityResponseType> {
    return this.http.patch<ITemas>(`${this.resourceUrl}/${this.getTemasIdentifier(temas)}`, temas, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITemas>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITemas[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTemasIdentifier(temas: Pick<ITemas, 'id'>): number {
    return temas.id;
  }

  compareTemas(o1: Pick<ITemas, 'id'> | null, o2: Pick<ITemas, 'id'> | null): boolean {
    return o1 && o2 ? this.getTemasIdentifier(o1) === this.getTemasIdentifier(o2) : o1 === o2;
  }

  addTemasToCollectionIfMissing<Type extends Pick<ITemas, 'id'>>(
    temasCollection: Type[],
    ...temasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const temas: Type[] = temasToCheck.filter(isPresent);
    if (temas.length > 0) {
      const temasCollectionIdentifiers = temasCollection.map(temasItem => this.getTemasIdentifier(temasItem)!);
      const temasToAdd = temas.filter(temasItem => {
        const temasIdentifier = this.getTemasIdentifier(temasItem);
        if (temasCollectionIdentifiers.includes(temasIdentifier)) {
          return false;
        }
        temasCollectionIdentifiers.push(temasIdentifier);
        return true;
      });
      return [...temasToAdd, ...temasCollection];
    }
    return temasCollection;
  }
}
