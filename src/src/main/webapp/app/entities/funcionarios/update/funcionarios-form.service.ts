import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFuncionarios, NewFuncionarios } from '../funcionarios.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFuncionarios for edit and NewFuncionariosFormGroupInput for create.
 */
type FuncionariosFormGroupInput = IFuncionarios | PartialWithRequiredKeyOf<NewFuncionarios>;

type FuncionariosFormDefaults = Pick<NewFuncionarios, 'id'>;

type FuncionariosFormGroupContent = {
  id: FormControl<IFuncionarios['id'] | NewFuncionarios['id']>;
  elo: FormControl<IFuncionarios['elo']>;
  fideId: FormControl<IFuncionarios['fideId']>;
  nombres: FormControl<IFuncionarios['nombres']>;
  apellidos: FormControl<IFuncionarios['apellidos']>;
  nombreCompleto: FormControl<IFuncionarios['nombreCompleto']>;
  email: FormControl<IFuncionarios['email']>;
  telefono: FormControl<IFuncionarios['telefono']>;
  fechaNacimiento: FormControl<IFuncionarios['fechaNacimiento']>;
  documento: FormControl<IFuncionarios['documento']>;
  estado: FormControl<IFuncionarios['estado']>;
  tipoFuncionario: FormControl<IFuncionarios['tipoFuncionario']>;
  tipoDocumentos: FormControl<IFuncionarios['tipoDocumentos']>;
};

export type FuncionariosFormGroup = FormGroup<FuncionariosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FuncionariosFormService {
  createFuncionariosFormGroup(funcionarios: FuncionariosFormGroupInput = { id: null }): FuncionariosFormGroup {
    const funcionariosRawValue = {
      ...this.getFormDefaults(),
      ...funcionarios,
    };
    return new FormGroup<FuncionariosFormGroupContent>({
      id: new FormControl(
        { value: funcionariosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      elo: new FormControl(funcionariosRawValue.elo),
      fideId: new FormControl(funcionariosRawValue.fideId),
      nombres: new FormControl(funcionariosRawValue.nombres, {
        validators: [Validators.required],
      }),
      apellidos: new FormControl(funcionariosRawValue.apellidos, {
        validators: [Validators.required],
      }),
      nombreCompleto: new FormControl(funcionariosRawValue.nombreCompleto, {
        validators: [Validators.required],
      }),
      email: new FormControl(funcionariosRawValue.email, {
        validators: [Validators.required],
      }),
      telefono: new FormControl(funcionariosRawValue.telefono, {
        validators: [Validators.required],
      }),
      fechaNacimiento: new FormControl(funcionariosRawValue.fechaNacimiento, {
        validators: [Validators.required],
      }),
      documento: new FormControl(funcionariosRawValue.documento, {
        validators: [Validators.required],
      }),
      estado: new FormControl(funcionariosRawValue.estado, {
        validators: [Validators.required],
      }),
      tipoFuncionario: new FormControl(funcionariosRawValue.tipoFuncionario),
      tipoDocumentos: new FormControl(funcionariosRawValue.tipoDocumentos, {
        validators: [Validators.required],
      }),
    });
  }

  getFuncionarios(form: FuncionariosFormGroup): IFuncionarios | NewFuncionarios {
    return form.getRawValue() as IFuncionarios | NewFuncionarios;
  }

  resetForm(form: FuncionariosFormGroup, funcionarios: FuncionariosFormGroupInput): void {
    const funcionariosRawValue = { ...this.getFormDefaults(), ...funcionarios };
    form.reset(
      {
        ...funcionariosRawValue,
        id: { value: funcionariosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FuncionariosFormDefaults {
    return {
      id: null,
    };
  }
}
