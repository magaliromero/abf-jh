import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../funcionarios.test-samples';

import { FuncionariosFormService } from './funcionarios-form.service';

describe('Funcionarios Form Service', () => {
  let service: FuncionariosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FuncionariosFormService);
  });

  describe('Service methods', () => {
    describe('createFuncionariosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFuncionariosFormGroup();

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
            tipoFuncionario: expect.any(Object),
            tipoDocumentos: expect.any(Object),
          })
        );
      });

      it('passing IFuncionarios should create a new form with FormGroup', () => {
        const formGroup = service.createFuncionariosFormGroup(sampleWithRequiredData);

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
            tipoFuncionario: expect.any(Object),
            tipoDocumentos: expect.any(Object),
          })
        );
      });
    });

    describe('getFuncionarios', () => {
      it('should return NewFuncionarios for default Funcionarios initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFuncionariosFormGroup(sampleWithNewData);

        const funcionarios = service.getFuncionarios(formGroup) as any;

        expect(funcionarios).toMatchObject(sampleWithNewData);
      });

      it('should return NewFuncionarios for empty Funcionarios initial value', () => {
        const formGroup = service.createFuncionariosFormGroup();

        const funcionarios = service.getFuncionarios(formGroup) as any;

        expect(funcionarios).toMatchObject({});
      });

      it('should return IFuncionarios', () => {
        const formGroup = service.createFuncionariosFormGroup(sampleWithRequiredData);

        const funcionarios = service.getFuncionarios(formGroup) as any;

        expect(funcionarios).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFuncionarios should not enable id FormControl', () => {
        const formGroup = service.createFuncionariosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFuncionarios should disable id FormControl', () => {
        const formGroup = service.createFuncionariosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
