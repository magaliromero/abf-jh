import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../inscripciones.test-samples';

import { InscripcionesFormService } from './inscripciones-form.service';

describe('Inscripciones Form Service', () => {
  let service: InscripcionesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscripcionesFormService);
  });

  describe('Service methods', () => {
    describe('createInscripcionesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInscripcionesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaInscripcion: expect.any(Object),
            alumnos: expect.any(Object),
            cursos: expect.any(Object),
          })
        );
      });

      it('passing IInscripciones should create a new form with FormGroup', () => {
        const formGroup = service.createInscripcionesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaInscripcion: expect.any(Object),
            alumnos: expect.any(Object),
            cursos: expect.any(Object),
          })
        );
      });
    });

    describe('getInscripciones', () => {
      it('should return NewInscripciones for default Inscripciones initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInscripcionesFormGroup(sampleWithNewData);

        const inscripciones = service.getInscripciones(formGroup) as any;

        expect(inscripciones).toMatchObject(sampleWithNewData);
      });

      it('should return NewInscripciones for empty Inscripciones initial value', () => {
        const formGroup = service.createInscripcionesFormGroup();

        const inscripciones = service.getInscripciones(formGroup) as any;

        expect(inscripciones).toMatchObject({});
      });

      it('should return IInscripciones', () => {
        const formGroup = service.createInscripcionesFormGroup(sampleWithRequiredData);

        const inscripciones = service.getInscripciones(formGroup) as any;

        expect(inscripciones).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInscripciones should not enable id FormControl', () => {
        const formGroup = service.createInscripcionesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInscripciones should disable id FormControl', () => {
        const formGroup = service.createInscripcionesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
