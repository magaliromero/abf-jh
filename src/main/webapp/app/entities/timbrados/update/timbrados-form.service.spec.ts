import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../timbrados.test-samples';

import { TimbradosFormService } from './timbrados-form.service';

describe('Timbrados Form Service', () => {
  let service: TimbradosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TimbradosFormService);
  });

  describe('Service methods', () => {
    describe('createTimbradosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTimbradosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroTimbrado: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            sucursales: expect.any(Object),
          })
        );
      });

      it('passing ITimbrados should create a new form with FormGroup', () => {
        const formGroup = service.createTimbradosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroTimbrado: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            sucursales: expect.any(Object),
          })
        );
      });
    });

    describe('getTimbrados', () => {
      it('should return NewTimbrados for default Timbrados initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTimbradosFormGroup(sampleWithNewData);

        const timbrados = service.getTimbrados(formGroup) as any;

        expect(timbrados).toMatchObject(sampleWithNewData);
      });

      it('should return NewTimbrados for empty Timbrados initial value', () => {
        const formGroup = service.createTimbradosFormGroup();

        const timbrados = service.getTimbrados(formGroup) as any;

        expect(timbrados).toMatchObject({});
      });

      it('should return ITimbrados', () => {
        const formGroup = service.createTimbradosFormGroup(sampleWithRequiredData);

        const timbrados = service.getTimbrados(formGroup) as any;

        expect(timbrados).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITimbrados should not enable id FormControl', () => {
        const formGroup = service.createTimbradosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTimbrados should disable id FormControl', () => {
        const formGroup = service.createTimbradosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
