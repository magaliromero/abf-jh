import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../facturas.test-samples';

import { FacturasFormService } from './facturas-form.service';

describe('Facturas Form Service', () => {
  let service: FacturasFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FacturasFormService);
  });

  describe('Service methods', () => {
    describe('createFacturasFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacturasFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            facturaNro: expect.any(Object),
            timbrado: expect.any(Object),
            razonSocial: expect.any(Object),
            ruc: expect.any(Object),
            condicionVenta: expect.any(Object),
            total: expect.any(Object),
          })
        );
      });

      it('passing IFacturas should create a new form with FormGroup', () => {
        const formGroup = service.createFacturasFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            facturaNro: expect.any(Object),
            timbrado: expect.any(Object),
            razonSocial: expect.any(Object),
            ruc: expect.any(Object),
            condicionVenta: expect.any(Object),
            total: expect.any(Object),
          })
        );
      });
    });

    describe('getFacturas', () => {
      it('should return NewFacturas for default Facturas initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFacturasFormGroup(sampleWithNewData);

        const facturas = service.getFacturas(formGroup) as any;

        expect(facturas).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacturas for empty Facturas initial value', () => {
        const formGroup = service.createFacturasFormGroup();

        const facturas = service.getFacturas(formGroup) as any;

        expect(facturas).toMatchObject({});
      });

      it('should return IFacturas', () => {
        const formGroup = service.createFacturasFormGroup(sampleWithRequiredData);

        const facturas = service.getFacturas(formGroup) as any;

        expect(facturas).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacturas should not enable id FormControl', () => {
        const formGroup = service.createFacturasFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacturas should disable id FormControl', () => {
        const formGroup = service.createFacturasFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
