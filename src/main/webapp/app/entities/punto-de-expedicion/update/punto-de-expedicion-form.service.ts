import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPuntoDeExpedicion, NewPuntoDeExpedicion } from '../punto-de-expedicion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPuntoDeExpedicion for edit and NewPuntoDeExpedicionFormGroupInput for create.
 */
type PuntoDeExpedicionFormGroupInput = IPuntoDeExpedicion | PartialWithRequiredKeyOf<NewPuntoDeExpedicion>;

type PuntoDeExpedicionFormDefaults = Pick<NewPuntoDeExpedicion, 'id'>;

type PuntoDeExpedicionFormGroupContent = {
  id: FormControl<IPuntoDeExpedicion['id'] | NewPuntoDeExpedicion['id']>;
  numeroPuntoDeExpedicion: FormControl<IPuntoDeExpedicion['numeroPuntoDeExpedicion']>;
  sucursales: FormControl<IPuntoDeExpedicion['sucursales']>;
};

export type PuntoDeExpedicionFormGroup = FormGroup<PuntoDeExpedicionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PuntoDeExpedicionFormService {
  createPuntoDeExpedicionFormGroup(puntoDeExpedicion: PuntoDeExpedicionFormGroupInput = { id: null }): PuntoDeExpedicionFormGroup {
    const puntoDeExpedicionRawValue = {
      ...this.getFormDefaults(),
      ...puntoDeExpedicion,
    };
    return new FormGroup<PuntoDeExpedicionFormGroupContent>({
      id: new FormControl(
        { value: puntoDeExpedicionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      numeroPuntoDeExpedicion: new FormControl(puntoDeExpedicionRawValue.numeroPuntoDeExpedicion, {
        validators: [Validators.required],
      }),
      sucursales: new FormControl(puntoDeExpedicionRawValue.sucursales, {
        validators: [Validators.required],
      }),
    });
  }

  getPuntoDeExpedicion(form: PuntoDeExpedicionFormGroup): IPuntoDeExpedicion | NewPuntoDeExpedicion {
    return form.getRawValue() as IPuntoDeExpedicion | NewPuntoDeExpedicion;
  }

  resetForm(form: PuntoDeExpedicionFormGroup, puntoDeExpedicion: PuntoDeExpedicionFormGroupInput): void {
    const puntoDeExpedicionRawValue = { ...this.getFormDefaults(), ...puntoDeExpedicion };
    form.reset(
      {
        ...puntoDeExpedicionRawValue,
        id: { value: puntoDeExpedicionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PuntoDeExpedicionFormDefaults {
    return {
      id: null,
    };
  }
}
