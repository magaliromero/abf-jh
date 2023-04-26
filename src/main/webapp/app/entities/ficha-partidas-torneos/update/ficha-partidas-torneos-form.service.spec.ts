import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ficha-partidas-torneos.test-samples';

import { FichaPartidasTorneosFormService } from './ficha-partidas-torneos-form.service';

describe('FichaPartidasTorneos Form Service', () => {
  let service: FichaPartidasTorneosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FichaPartidasTorneosFormService);
  });

  describe('Service methods', () => {
    describe('createFichaPartidasTorneosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFichaPartidasTorneosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreContrincante: expect.any(Object),
            duracion: expect.any(Object),
            winner: expect.any(Object),
            resultado: expect.any(Object),
            comentarios: expect.any(Object),
            nombreArbitro: expect.any(Object),
            torneos: expect.any(Object),
            alumnos: expect.any(Object),
          })
        );
      });

      it('passing IFichaPartidasTorneos should create a new form with FormGroup', () => {
        const formGroup = service.createFichaPartidasTorneosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombreContrincante: expect.any(Object),
            duracion: expect.any(Object),
            winner: expect.any(Object),
            resultado: expect.any(Object),
            comentarios: expect.any(Object),
            nombreArbitro: expect.any(Object),
            torneos: expect.any(Object),
            alumnos: expect.any(Object),
          })
        );
      });
    });

    describe('getFichaPartidasTorneos', () => {
      it('should return NewFichaPartidasTorneos for default FichaPartidasTorneos initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFichaPartidasTorneosFormGroup(sampleWithNewData);

        const fichaPartidasTorneos = service.getFichaPartidasTorneos(formGroup) as any;

        expect(fichaPartidasTorneos).toMatchObject(sampleWithNewData);
      });

      it('should return NewFichaPartidasTorneos for empty FichaPartidasTorneos initial value', () => {
        const formGroup = service.createFichaPartidasTorneosFormGroup();

        const fichaPartidasTorneos = service.getFichaPartidasTorneos(formGroup) as any;

        expect(fichaPartidasTorneos).toMatchObject({});
      });

      it('should return IFichaPartidasTorneos', () => {
        const formGroup = service.createFichaPartidasTorneosFormGroup(sampleWithRequiredData);

        const fichaPartidasTorneos = service.getFichaPartidasTorneos(formGroup) as any;

        expect(fichaPartidasTorneos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFichaPartidasTorneos should not enable id FormControl', () => {
        const formGroup = service.createFichaPartidasTorneosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFichaPartidasTorneos should disable id FormControl', () => {
        const formGroup = service.createFichaPartidasTorneosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
