import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cursos.test-samples';

import { CursosFormService } from './cursos-form.service';

describe('Cursos Form Service', () => {
  let service: CursosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CursosFormService);
  });

  describe('Service methods', () => {
    describe('createCursosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCursosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreCurso: expect.any(Object),
            descripcion: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            cantidadClases: expect.any(Object),
            nivel: expect.any(Object),
            temas: expect.any(Object),
          })
        );
      });

      it('passing ICursos should create a new form with FormGroup', () => {
        const formGroup = service.createCursosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreCurso: expect.any(Object),
            descripcion: expect.any(Object),
            fechaInicio: expect.any(Object),
            fechaFin: expect.any(Object),
            cantidadClases: expect.any(Object),
            nivel: expect.any(Object),
            temas: expect.any(Object),
          })
        );
      });
    });

    describe('getCursos', () => {
      it('should return NewCursos for default Cursos initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCursosFormGroup(sampleWithNewData);

        const cursos = service.getCursos(formGroup) as any;

        expect(cursos).toMatchObject(sampleWithNewData);
      });

      it('should return NewCursos for empty Cursos initial value', () => {
        const formGroup = service.createCursosFormGroup();

        const cursos = service.getCursos(formGroup) as any;

        expect(cursos).toMatchObject({});
      });

      it('should return ICursos', () => {
        const formGroup = service.createCursosFormGroup(sampleWithRequiredData);

        const cursos = service.getCursos(formGroup) as any;

        expect(cursos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICursos should not enable id FormControl', () => {
        const formGroup = service.createCursosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCursos should disable id FormControl', () => {
        const formGroup = service.createCursosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
