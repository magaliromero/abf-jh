import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../evaluaciones.test-samples';

import { EvaluacionesFormService } from './evaluaciones-form.service';

describe('Evaluaciones Form Service', () => {
  let service: EvaluacionesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvaluacionesFormService);
  });

  describe('Service methods', () => {
    describe('createEvaluacionesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvaluacionesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nroEvaluacion: expect.any(Object),
            fecha: expect.any(Object),
            alumnos: expect.any(Object),
            funcionarios: expect.any(Object),
          })
        );
      });

      it('passing IEvaluaciones should create a new form with FormGroup', () => {
        const formGroup = service.createEvaluacionesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nroEvaluacion: expect.any(Object),
            fecha: expect.any(Object),
            alumnos: expect.any(Object),
            funcionarios: expect.any(Object),
          })
        );
      });
    });

    describe('getEvaluaciones', () => {
      it('should return NewEvaluaciones for default Evaluaciones initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEvaluacionesFormGroup(sampleWithNewData);

        const evaluaciones = service.getEvaluaciones(formGroup) as any;

        expect(evaluaciones).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvaluaciones for empty Evaluaciones initial value', () => {
        const formGroup = service.createEvaluacionesFormGroup();

        const evaluaciones = service.getEvaluaciones(formGroup) as any;

        expect(evaluaciones).toMatchObject({});
      });

      it('should return IEvaluaciones', () => {
        const formGroup = service.createEvaluacionesFormGroup(sampleWithRequiredData);

        const evaluaciones = service.getEvaluaciones(formGroup) as any;

        expect(evaluaciones).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvaluaciones should not enable id FormControl', () => {
        const formGroup = service.createEvaluacionesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvaluaciones should disable id FormControl', () => {
        const formGroup = service.createEvaluacionesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
