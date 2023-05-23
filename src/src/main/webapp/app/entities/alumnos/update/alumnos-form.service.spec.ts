import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../alumnos.test-samples';

import { AlumnosFormService } from './alumnos-form.service';

describe('Alumnos Form Service', () => {
  let service: AlumnosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlumnosFormService);
  });

  describe('Service methods', () => {
    describe('createAlumnosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlumnosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            elo: expect.any(Object),
            fideId: expect.any(Object),
            nombres: expect.any(Object),
            apellidos: expect.any(Object),
            nombreCompleto: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            fechaNacimiento: expect.any(Object),
            documento: expect.any(Object),
            estado: expect.any(Object),
            tipoDocumentos: expect.any(Object),
          })
        );
      });

      it('passing IAlumnos should create a new form with FormGroup', () => {
        const formGroup = service.createAlumnosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            elo: expect.any(Object),
            fideId: expect.any(Object),
            nombres: expect.any(Object),
            apellidos: expect.any(Object),
            nombreCompleto: expect.any(Object),
            email: expect.any(Object),
            telefono: expect.any(Object),
            fechaNacimiento: expect.any(Object),
            documento: expect.any(Object),
            estado: expect.any(Object),
            tipoDocumentos: expect.any(Object),
          })
        );
      });
    });

    describe('getAlumnos', () => {
      it('should return NewAlumnos for default Alumnos initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAlumnosFormGroup(sampleWithNewData);

        const alumnos = service.getAlumnos(formGroup) as any;

        expect(alumnos).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlumnos for empty Alumnos initial value', () => {
        const formGroup = service.createAlumnosFormGroup();

        const alumnos = service.getAlumnos(formGroup) as any;

        expect(alumnos).toMatchObject({});
      });

      it('should return IAlumnos', () => {
        const formGroup = service.createAlumnosFormGroup(sampleWithRequiredData);

        const alumnos = service.getAlumnos(formGroup) as any;

        expect(alumnos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlumnos should not enable id FormControl', () => {
        const formGroup = service.createAlumnosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlumnos should disable id FormControl', () => {
        const formGroup = service.createAlumnosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
