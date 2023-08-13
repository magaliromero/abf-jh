import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../registro-clases.test-samples';

import { RegistroClasesFormService } from './registro-clases-form.service';

describe('RegistroClases Form Service', () => {
  let service: RegistroClasesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegistroClasesFormService);
  });

  describe('Service methods', () => {
    describe('createRegistroClasesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRegistroClasesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            cantidadHoras: expect.any(Object),
            asistenciaAlumno: expect.any(Object),
            temas: expect.any(Object),
            funcionario: expect.any(Object),
            alumnos: expect.any(Object),
          })
        );
      });

      it('passing IRegistroClases should create a new form with FormGroup', () => {
        const formGroup = service.createRegistroClasesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            cantidadHoras: expect.any(Object),
            asistenciaAlumno: expect.any(Object),
            temas: expect.any(Object),
            funcionario: expect.any(Object),
            alumnos: expect.any(Object),
          })
        );
      });
    });

    describe('getRegistroClases', () => {
      it('should return NewRegistroClases for default RegistroClases initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRegistroClasesFormGroup(sampleWithNewData);

        const registroClases = service.getRegistroClases(formGroup) as any;

        expect(registroClases).toMatchObject(sampleWithNewData);
      });

      it('should return NewRegistroClases for empty RegistroClases initial value', () => {
        const formGroup = service.createRegistroClasesFormGroup();

        const registroClases = service.getRegistroClases(formGroup) as any;

        expect(registroClases).toMatchObject({});
      });

      it('should return IRegistroClases', () => {
        const formGroup = service.createRegistroClasesFormGroup(sampleWithRequiredData);

        const registroClases = service.getRegistroClases(formGroup) as any;

        expect(registroClases).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRegistroClases should not enable id FormControl', () => {
        const formGroup = service.createRegistroClasesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRegistroClases should disable id FormControl', () => {
        const formGroup = service.createRegistroClasesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
