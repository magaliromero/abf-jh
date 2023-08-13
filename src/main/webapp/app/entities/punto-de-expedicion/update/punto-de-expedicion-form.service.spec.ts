import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../punto-de-expedicion.test-samples';

import { PuntoDeExpedicionFormService } from './punto-de-expedicion-form.service';

describe('PuntoDeExpedicion Form Service', () => {
  let service: PuntoDeExpedicionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PuntoDeExpedicionFormService);
  });

  describe('Service methods', () => {
    describe('createPuntoDeExpedicionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPuntoDeExpedicionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroPuntoDeExpedicion: expect.any(Object),
            sucursales: expect.any(Object),
          })
        );
      });

      it('passing IPuntoDeExpedicion should create a new form with FormGroup', () => {
        const formGroup = service.createPuntoDeExpedicionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numeroPuntoDeExpedicion: expect.any(Object),
            sucursales: expect.any(Object),
          })
        );
      });
    });

    describe('getPuntoDeExpedicion', () => {
      it('should return NewPuntoDeExpedicion for default PuntoDeExpedicion initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPuntoDeExpedicionFormGroup(sampleWithNewData);

        const puntoDeExpedicion = service.getPuntoDeExpedicion(formGroup) as any;

        expect(puntoDeExpedicion).toMatchObject(sampleWithNewData);
      });

      it('should return NewPuntoDeExpedicion for empty PuntoDeExpedicion initial value', () => {
        const formGroup = service.createPuntoDeExpedicionFormGroup();

        const puntoDeExpedicion = service.getPuntoDeExpedicion(formGroup) as any;

        expect(puntoDeExpedicion).toMatchObject({});
      });

      it('should return IPuntoDeExpedicion', () => {
        const formGroup = service.createPuntoDeExpedicionFormGroup(sampleWithRequiredData);

        const puntoDeExpedicion = service.getPuntoDeExpedicion(formGroup) as any;

        expect(puntoDeExpedicion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPuntoDeExpedicion should not enable id FormControl', () => {
        const formGroup = service.createPuntoDeExpedicionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPuntoDeExpedicion should disable id FormControl', () => {
        const formGroup = service.createPuntoDeExpedicionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
