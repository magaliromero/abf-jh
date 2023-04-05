import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cobros.test-samples';

import { CobrosFormService } from './cobros-form.service';

describe('Cobros Form Service', () => {
  let service: CobrosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CobrosFormService);
  });

  describe('Service methods', () => {
    describe('createCobrosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCobrosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            montoPago: expect.any(Object),
            montoInicial: expect.any(Object),
            saldo: expect.any(Object),
            fechaRegistro: expect.any(Object),
            fechaPago: expect.any(Object),
            tipoPago: expect.any(Object),
            descripcion: expect.any(Object),
            alumnos: expect.any(Object),
          })
        );
      });

      it('passing ICobros should create a new form with FormGroup', () => {
        const formGroup = service.createCobrosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            montoPago: expect.any(Object),
            montoInicial: expect.any(Object),
            saldo: expect.any(Object),
            fechaRegistro: expect.any(Object),
            fechaPago: expect.any(Object),
            tipoPago: expect.any(Object),
            descripcion: expect.any(Object),
            alumnos: expect.any(Object),
          })
        );
      });
    });

    describe('getCobros', () => {
      it('should return NewCobros for default Cobros initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCobrosFormGroup(sampleWithNewData);

        const cobros = service.getCobros(formGroup) as any;

        expect(cobros).toMatchObject(sampleWithNewData);
      });

      it('should return NewCobros for empty Cobros initial value', () => {
        const formGroup = service.createCobrosFormGroup();

        const cobros = service.getCobros(formGroup) as any;

        expect(cobros).toMatchObject({});
      });

      it('should return ICobros', () => {
        const formGroup = service.createCobrosFormGroup(sampleWithRequiredData);

        const cobros = service.getCobros(formGroup) as any;

        expect(cobros).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICobros should not enable id FormControl', () => {
        const formGroup = service.createCobrosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCobros should disable id FormControl', () => {
        const formGroup = service.createCobrosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
