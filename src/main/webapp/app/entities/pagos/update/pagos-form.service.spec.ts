import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pagos.test-samples';

import { PagosFormService } from './pagos-form.service';

describe('Pagos Form Service', () => {
  let service: PagosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PagosFormService);
  });

  describe('Service methods', () => {
    describe('createPagosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPagosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            total: expect.any(Object),
            cantidadHoras: expect.any(Object),
            producto: expect.any(Object),
            funcionario: expect.any(Object),
          })
        );
      });

      it('passing IPagos should create a new form with FormGroup', () => {
        const formGroup = service.createPagosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            total: expect.any(Object),
            cantidadHoras: expect.any(Object),
            producto: expect.any(Object),
            funcionario: expect.any(Object),
          })
        );
      });
    });

    describe('getPagos', () => {
      it('should return NewPagos for default Pagos initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPagosFormGroup(sampleWithNewData);

        const pagos = service.getPagos(formGroup) as any;

        expect(pagos).toMatchObject(sampleWithNewData);
      });

      it('should return NewPagos for empty Pagos initial value', () => {
        const formGroup = service.createPagosFormGroup();

        const pagos = service.getPagos(formGroup) as any;

        expect(pagos).toMatchObject({});
      });

      it('should return IPagos', () => {
        const formGroup = service.createPagosFormGroup(sampleWithRequiredData);

        const pagos = service.getPagos(formGroup) as any;

        expect(pagos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPagos should not enable id FormControl', () => {
        const formGroup = service.createPagosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPagos should disable id FormControl', () => {
        const formGroup = service.createPagosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
