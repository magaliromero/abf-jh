import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../prestamos.test-samples';

import { PrestamosFormService } from './prestamos-form.service';

describe('Prestamos Form Service', () => {
  let service: PrestamosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrestamosFormService);
  });

  describe('Service methods', () => {
    describe('createPrestamosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrestamosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaPrestamo: expect.any(Object),
            fechaDevolucion: expect.any(Object),
            estado: expect.any(Object),
            materiales: expect.any(Object),
            alumnos: expect.any(Object),
          })
        );
      });

      it('passing IPrestamos should create a new form with FormGroup', () => {
        const formGroup = service.createPrestamosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fechaPrestamo: expect.any(Object),
            fechaDevolucion: expect.any(Object),
            estado: expect.any(Object),
            materiales: expect.any(Object),
            alumnos: expect.any(Object),
          })
        );
      });
    });

    describe('getPrestamos', () => {
      it('should return NewPrestamos for default Prestamos initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPrestamosFormGroup(sampleWithNewData);

        const prestamos = service.getPrestamos(formGroup) as any;

        expect(prestamos).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrestamos for empty Prestamos initial value', () => {
        const formGroup = service.createPrestamosFormGroup();

        const prestamos = service.getPrestamos(formGroup) as any;

        expect(prestamos).toMatchObject({});
      });

      it('should return IPrestamos', () => {
        const formGroup = service.createPrestamosFormGroup(sampleWithRequiredData);

        const prestamos = service.getPrestamos(formGroup) as any;

        expect(prestamos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrestamos should not enable id FormControl', () => {
        const formGroup = service.createPrestamosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrestamos should disable id FormControl', () => {
        const formGroup = service.createPrestamosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
