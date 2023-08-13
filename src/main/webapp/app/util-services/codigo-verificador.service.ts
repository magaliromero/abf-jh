import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class CodigoVerificadorService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('/digito/codigo/');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  obtenerRuc(documento: String) {
    return this.http.get(this.resourceUrl + documento, { observe: 'response' });
  }
}
