import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tipos-documentos.test-samples';

import { TiposDocumentosFormService } from './tipos-documentos-form.service';

describe('TiposDocumentos Form Service', () => {
  let service: TiposDocumentosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TiposDocumentosFormService);
  });

  describe('Service methods', () => {
    describe('createTiposDocumentosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTiposDocumentosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            descripcion: expect.any(Object),
          })
        );
      });

      it('passing ITiposDocumentos should create a new form with FormGroup', () => {
        const formGroup = service.createTiposDocumentosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            descripcion: expect.any(Object),
          })
        );
      });
    });

    describe('getTiposDocumentos', () => {
      it('should return NewTiposDocumentos for default TiposDocumentos initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTiposDocumentosFormGroup(sampleWithNewData);

        const tiposDocumentos = service.getTiposDocumentos(formGroup) as any;

        expect(tiposDocumentos).toMatchObject(sampleWithNewData);
      });

      it('should return NewTiposDocumentos for empty TiposDocumentos initial value', () => {
        const formGroup = service.createTiposDocumentosFormGroup();

        const tiposDocumentos = service.getTiposDocumentos(formGroup) as any;

        expect(tiposDocumentos).toMatchObject({});
      });

      it('should return ITiposDocumentos', () => {
        const formGroup = service.createTiposDocumentosFormGroup(sampleWithRequiredData);

        const tiposDocumentos = service.getTiposDocumentos(formGroup) as any;

        expect(tiposDocumentos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITiposDocumentos should not enable id FormControl', () => {
        const formGroup = service.createTiposDocumentosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTiposDocumentos should disable id FormControl', () => {
        const formGroup = service.createTiposDocumentosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
