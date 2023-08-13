import { TestBed } from '@angular/core/testing';

import { CodigoVerificadorService } from './codigo-verificador.service';

describe('CodigoVerificadorService', () => {
  let service: CodigoVerificadorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CodigoVerificadorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
