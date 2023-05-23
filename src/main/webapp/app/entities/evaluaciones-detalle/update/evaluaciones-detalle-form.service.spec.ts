import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../evaluaciones-detalle.test-samples';

import { EvaluacionesDetalleFormService } from './evaluaciones-detalle-form.service';

describe('EvaluacionesDetalle Form Service', () => {
  let service: EvaluacionesDetalleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvaluacionesDetalleFormService);
  });

  describe('Service methods', () => {
    describe('createEvaluacionesDetalleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvaluacionesDetalleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            comentarios: expect.any(Object),
            puntaje: expect.any(Object),
            evaluaciones: expect.any(Object),
            temas: expect.any(Object),
          })
        );
      });

      it('passing IEvaluacionesDetalle should create a new form with FormGroup', () => {
        const formGroup = service.createEvaluacionesDetalleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            comentarios: expect.any(Object),
            puntaje: expect.any(Object),
            evaluaciones: expect.any(Object),
            temas: expect.any(Object),
          })
        );
      });
    });

    describe('getEvaluacionesDetalle', () => {
      it('should return NewEvaluacionesDetalle for default EvaluacionesDetalle initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEvaluacionesDetalleFormGroup(sampleWithNewData);

        const evaluacionesDetalle = service.getEvaluacionesDetalle(formGroup) as any;

        expect(evaluacionesDetalle).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvaluacionesDetalle for empty EvaluacionesDetalle initial value', () => {
        const formGroup = service.createEvaluacionesDetalleFormGroup();

        const evaluacionesDetalle = service.getEvaluacionesDetalle(formGroup) as any;

        expect(evaluacionesDetalle).toMatchObject({});
      });

      it('should return IEvaluacionesDetalle', () => {
        const formGroup = service.createEvaluacionesDetalleFormGroup(sampleWithRequiredData);

        const evaluacionesDetalle = service.getEvaluacionesDetalle(formGroup) as any;

        expect(evaluacionesDetalle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvaluacionesDetalle should not enable id FormControl', () => {
        const formGroup = service.createEvaluacionesDetalleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvaluacionesDetalle should disable id FormControl', () => {
        const formGroup = service.createEvaluacionesDetalleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
