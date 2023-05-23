import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../materiales.test-samples';

import { MaterialesFormService } from './materiales-form.service';

describe('Materiales Form Service', () => {
  let service: MaterialesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialesFormService);
  });

  describe('Service methods', () => {
    describe('createMaterialesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaterialesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
            cantidad: expect.any(Object),
            cantidadEnPrestamo: expect.any(Object),
          })
        );
      });

      it('passing IMateriales should create a new form with FormGroup', () => {
        const formGroup = service.createMaterialesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descripcion: expect.any(Object),
            cantidad: expect.any(Object),
            cantidadEnPrestamo: expect.any(Object),
          })
        );
      });
    });

    describe('getMateriales', () => {
      it('should return NewMateriales for default Materiales initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMaterialesFormGroup(sampleWithNewData);

        const materiales = service.getMateriales(formGroup) as any;

        expect(materiales).toMatchObject(sampleWithNewData);
      });

      it('should return NewMateriales for empty Materiales initial value', () => {
        const formGroup = service.createMaterialesFormGroup();

        const materiales = service.getMateriales(formGroup) as any;

        expect(materiales).toMatchObject({});
      });

      it('should return IMateriales', () => {
        const formGroup = service.createMaterialesFormGroup(sampleWithRequiredData);

        const materiales = service.getMateriales(formGroup) as any;

        expect(materiales).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMateriales should not enable id FormControl', () => {
        const formGroup = service.createMaterialesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMateriales should disable id FormControl', () => {
        const formGroup = service.createMaterialesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
