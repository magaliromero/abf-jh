import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IClientes, NewClientes } from '../clientes.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClientes for edit and NewClientesFormGroupInput for create.
 */
type ClientesFormGroupInput = IClientes | PartialWithRequiredKeyOf<NewClientes>;

type ClientesFormDefaults = Pick<NewClientes, 'id'>;

type ClientesFormGroupContent = {
  id: FormControl<IClientes['id'] | NewClientes['id']>;
  ruc: FormControl<IClientes['ruc']>;
  nombres: FormControl<IClientes['nombres']>;
  apellidos: FormControl<IClientes['apellidos']>;
  razonSocial: FormControl<IClientes['razonSocial']>;
  documento: FormControl<IClientes['documento']>;
  email: FormControl<IClientes['email']>;
  telefono: FormControl<IClientes['telefono']>;
  fechaNacimiento: FormControl<IClientes['fechaNacimiento']>;
  direccion: FormControl<IClientes['direccion']>;
};

export type ClientesFormGroup = FormGroup<ClientesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientesFormService {
  createClientesFormGroup(clientes: ClientesFormGroupInput = { id: null }): ClientesFormGroup {
    const clientesRawValue = {
      ...this.getFormDefaults(),
      ...clientes,
    };
    return new FormGroup<ClientesFormGroupContent>({
      id: new FormControl(
        { value: clientesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      ruc: new FormControl(clientesRawValue.ruc),
      nombres: new FormControl(clientesRawValue.nombres, {
        validators: [Validators.required],
      }),
      apellidos: new FormControl(clientesRawValue.apellidos, {
        validators: [Validators.required],
      }),
      razonSocial: new FormControl(clientesRawValue.razonSocial, {
        validators: [Validators.required],
      }),
      documento: new FormControl(clientesRawValue.documento, {
        validators: [Validators.required],
      }),
      email: new FormControl(clientesRawValue.email),
      telefono: new FormControl(clientesRawValue.telefono),
      fechaNacimiento: new FormControl(clientesRawValue.fechaNacimiento),
      direccion: new FormControl(clientesRawValue.direccion),
    });
  }

  getClientes(form: ClientesFormGroup): IClientes | NewClientes {
    return form.getRawValue() as IClientes | NewClientes;
  }

  resetForm(form: ClientesFormGroup, clientes: ClientesFormGroupInput): void {
    const clientesRawValue = { ...this.getFormDefaults(), ...clientes };
    form.reset(
      {
        ...clientesRawValue,
        id: { value: clientesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ClientesFormDefaults {
    return {
      id: null,
    };
  }
}
