import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITiposDocumentos, NewTiposDocumentos } from '../tipos-documentos.model';

export type PartialUpdateTiposDocumentos = Partial<ITiposDocumentos> & Pick<ITiposDocumentos, 'id'>;

export type EntityResponseType = HttpResponse<ITiposDocumentos>;
export type EntityArrayResponseType = HttpResponse<ITiposDocumentos[]>;

@Injectable({ providedIn: 'root' })
export class TiposDocumentosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tipos-documentos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tiposDocumentos: NewTiposDocumentos): Observable<EntityResponseType> {
    return this.http.post<ITiposDocumentos>(this.resourceUrl, tiposDocumentos, { observe: 'response' });
  }

  update(tiposDocumentos: ITiposDocumentos): Observable<EntityResponseType> {
    return this.http.put<ITiposDocumentos>(`${this.resourceUrl}/${this.getTiposDocumentosIdentifier(tiposDocumentos)}`, tiposDocumentos, {
      observe: 'response',
    });
  }

  partialUpdate(tiposDocumentos: PartialUpdateTiposDocumentos): Observable<EntityResponseType> {
    return this.http.patch<ITiposDocumentos>(`${this.resourceUrl}/${this.getTiposDocumentosIdentifier(tiposDocumentos)}`, tiposDocumentos, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITiposDocumentos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITiposDocumentos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTiposDocumentosIdentifier(tiposDocumentos: Pick<ITiposDocumentos, 'id'>): number {
    return tiposDocumentos.id;
  }

  compareTiposDocumentos(o1: Pick<ITiposDocumentos, 'id'> | null, o2: Pick<ITiposDocumentos, 'id'> | null): boolean {
    return o1 && o2 ? this.getTiposDocumentosIdentifier(o1) === this.getTiposDocumentosIdentifier(o2) : o1 === o2;
  }

  addTiposDocumentosToCollectionIfMissing<Type extends Pick<ITiposDocumentos, 'id'>>(
    tiposDocumentosCollection: Type[],
    ...tiposDocumentosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tiposDocumentos: Type[] = tiposDocumentosToCheck.filter(isPresent);
    if (tiposDocumentos.length > 0) {
      const tiposDocumentosCollectionIdentifiers = tiposDocumentosCollection.map(
        tiposDocumentosItem => this.getTiposDocumentosIdentifier(tiposDocumentosItem)!
      );
      const tiposDocumentosToAdd = tiposDocumentos.filter(tiposDocumentosItem => {
        const tiposDocumentosIdentifier = this.getTiposDocumentosIdentifier(tiposDocumentosItem);
        if (tiposDocumentosCollectionIdentifiers.includes(tiposDocumentosIdentifier)) {
          return false;
        }
        tiposDocumentosCollectionIdentifiers.push(tiposDocumentosIdentifier);
        return true;
      });
      return [...tiposDocumentosToAdd, ...tiposDocumentosCollection];
    }
    return tiposDocumentosCollection;
  }
}
