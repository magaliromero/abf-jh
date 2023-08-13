import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../temas.test-samples';

import { TemasFormService } from './temas-form.service';

describe('Temas Form Service', () => {
  let service: TemasFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TemasFormService);
  });

  describe('Service methods', () => {
    describe('createTemasFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTemasFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
            descripcion: expect.any(Object),
          })
        );
      });

      it('passing ITemas should create a new form with FormGroup', () => {
        const formGroup = service.createTemasFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
            descripcion: expect.any(Object),
          })
        );
      });
    });

    describe('getTemas', () => {
      it('should return NewTemas for default Temas initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTemasFormGroup(sampleWithNewData);

        const temas = service.getTemas(formGroup) as any;

        expect(temas).toMatchObject(sampleWithNewData);
      });

      it('should return NewTemas for empty Temas initial value', () => {
        const formGroup = service.createTemasFormGroup();

        const temas = service.getTemas(formGroup) as any;

        expect(temas).toMatchObject({});
      });

      it('should return ITemas', () => {
        const formGroup = service.createTemasFormGroup(sampleWithRequiredData);

        const temas = service.getTemas(formGroup) as any;

        expect(temas).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITemas should not enable id FormControl', () => {
        const formGroup = service.createTemasFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTemas should disable id FormControl', () => {
        const formGroup = service.createTemasFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
