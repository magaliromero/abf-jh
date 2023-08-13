import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sucursales.test-samples';

import { SucursalesFormService } from './sucursales-form.service';

describe('Sucursales Form Service', () => {
  let service: SucursalesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SucursalesFormService);
  });

  describe('Service methods', () => {
    describe('createSucursalesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSucursalesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreSucursal: expect.any(Object),
            direccion: expect.any(Object),
            numeroEstablecimiento: expect.any(Object),
          })
        );
      });

      it('passing ISucursales should create a new form with FormGroup', () => {
        const formGroup = service.createSucursalesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreSucursal: expect.any(Object),
            direccion: expect.any(Object),
            numeroEstablecimiento: expect.any(Object),
          })
        );
      });
    });

    describe('getSucursales', () => {
      it('should return NewSucursales for default Sucursales initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSucursalesFormGroup(sampleWithNewData);

        const sucursales = service.getSucursales(formGroup) as any;

        expect(sucursales).toMatchObject(sampleWithNewData);
      });

      it('should return NewSucursales for empty Sucursales initial value', () => {
        const formGroup = service.createSucursalesFormGroup();

        const sucursales = service.getSucursales(formGroup) as any;

        expect(sucursales).toMatchObject({});
      });

      it('should return ISucursales', () => {
        const formGroup = service.createSucursalesFormGroup(sampleWithRequiredData);

        const sucursales = service.getSucursales(formGroup) as any;

        expect(sucursales).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISucursales should not enable id FormControl', () => {
        const formGroup = service.createSucursalesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSucursales should disable id FormControl', () => {
        const formGroup = service.createSucursalesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
