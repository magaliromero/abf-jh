import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../factura-detalle.test-samples';

import { FacturaDetalleFormService } from './factura-detalle-form.service';

describe('FacturaDetalle Form Service', () => {
  let service: FacturaDetalleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FacturaDetalleFormService);
  });

  describe('Service methods', () => {
    describe('createFacturaDetalleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFacturaDetalleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidad: expect.any(Object),
            precioUnitario: expect.any(Object),
            subtotal: expect.any(Object),
            porcentajeIva: expect.any(Object),
            valorPorcentaje: expect.any(Object),
            producto: expect.any(Object),
            factura: expect.any(Object),
          })
        );
      });

      it('passing IFacturaDetalle should create a new form with FormGroup', () => {
        const formGroup = service.createFacturaDetalleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidad: expect.any(Object),
            precioUnitario: expect.any(Object),
            subtotal: expect.any(Object),
            porcentajeIva: expect.any(Object),
            valorPorcentaje: expect.any(Object),
            producto: expect.any(Object),
            factura: expect.any(Object),
          })
        );
      });
    });

    describe('getFacturaDetalle', () => {
      it('should return NewFacturaDetalle for default FacturaDetalle initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFacturaDetalleFormGroup(sampleWithNewData);

        const facturaDetalle = service.getFacturaDetalle(formGroup) as any;

        expect(facturaDetalle).toMatchObject(sampleWithNewData);
      });

      it('should return NewFacturaDetalle for empty FacturaDetalle initial value', () => {
        const formGroup = service.createFacturaDetalleFormGroup();

        const facturaDetalle = service.getFacturaDetalle(formGroup) as any;

        expect(facturaDetalle).toMatchObject({});
      });

      it('should return IFacturaDetalle', () => {
        const formGroup = service.createFacturaDetalleFormGroup(sampleWithRequiredData);

        const facturaDetalle = service.getFacturaDetalle(formGroup) as any;

        expect(facturaDetalle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFacturaDetalle should not enable id FormControl', () => {
        const formGroup = service.createFacturaDetalleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFacturaDetalle should disable id FormControl', () => {
        const formGroup = service.createFacturaDetalleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
