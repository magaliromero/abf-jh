import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvaluacionesDetalle, NewEvaluacionesDetalle } from '../evaluaciones-detalle.model';

export type PartialUpdateEvaluacionesDetalle = Partial<IEvaluacionesDetalle> & Pick<IEvaluacionesDetalle, 'id'>;

export type EntityResponseType = HttpResponse<IEvaluacionesDetalle>;
export type EntityArrayResponseType = HttpResponse<IEvaluacionesDetalle[]>;

@Injectable({ providedIn: 'root' })
export class EvaluacionesDetalleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evaluaciones-detalles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(evaluacionesDetalle: NewEvaluacionesDetalle): Observable<EntityResponseType> {
    return this.http.post<IEvaluacionesDetalle>(this.resourceUrl, evaluacionesDetalle, { observe: 'response' });
  }

  update(evaluacionesDetalle: IEvaluacionesDetalle): Observable<EntityResponseType> {
    return this.http.put<IEvaluacionesDetalle>(
      `${this.resourceUrl}/${this.getEvaluacionesDetalleIdentifier(evaluacionesDetalle)}`,
      evaluacionesDetalle,
      { observe: 'response' }
    );
  }

  partialUpdate(evaluacionesDetalle: PartialUpdateEvaluacionesDetalle): Observable<EntityResponseType> {
    return this.http.patch<IEvaluacionesDetalle>(
      `${this.resourceUrl}/${this.getEvaluacionesDetalleIdentifier(evaluacionesDetalle)}`,
      evaluacionesDetalle,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEvaluacionesDetalle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEvaluacionesDetalle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEvaluacionesDetalleIdentifier(evaluacionesDetalle: Pick<IEvaluacionesDetalle, 'id'>): number {
    return evaluacionesDetalle.id;
  }

  compareEvaluacionesDetalle(o1: Pick<IEvaluacionesDetalle, 'id'> | null, o2: Pick<IEvaluacionesDetalle, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvaluacionesDetalleIdentifier(o1) === this.getEvaluacionesDetalleIdentifier(o2) : o1 === o2;
  }

  addEvaluacionesDetalleToCollectionIfMissing<Type extends Pick<IEvaluacionesDetalle, 'id'>>(
    evaluacionesDetalleCollection: Type[],
    ...evaluacionesDetallesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evaluacionesDetalles: Type[] = evaluacionesDetallesToCheck.filter(isPresent);
    if (evaluacionesDetalles.length > 0) {
      const evaluacionesDetalleCollectionIdentifiers = evaluacionesDetalleCollection.map(
        evaluacionesDetalleItem => this.getEvaluacionesDetalleIdentifier(evaluacionesDetalleItem)!
      );
      const evaluacionesDetallesToAdd = evaluacionesDetalles.filter(evaluacionesDetalleItem => {
        const evaluacionesDetalleIdentifier = this.getEvaluacionesDetalleIdentifier(evaluacionesDetalleItem);
        if (evaluacionesDetalleCollectionIdentifiers.includes(evaluacionesDetalleIdentifier)) {
          return false;
        }
        evaluacionesDetalleCollectionIdentifiers.push(evaluacionesDetalleIdentifier);
        return true;
      });
      return [...evaluacionesDetallesToAdd, ...evaluacionesDetalleCollection];
    }
    return evaluacionesDetalleCollection;
  }
}
