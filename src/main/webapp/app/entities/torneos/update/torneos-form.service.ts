import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITorneos, NewTorneos } from '../torneos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITorneos for edit and NewTorneosFormGroupInput for create.
 */
type TorneosFormGroupInput = ITorneos | PartialWithRequiredKeyOf<NewTorneos>;

type TorneosFormDefaults = Pick<NewTorneos, 'id' | 'torneoEvaluado' | 'federado'>;

type TorneosFormGroupContent = {
  id: FormControl<ITorneos['id'] | NewTorneos['id']>;
  nombreTorneo: FormControl<ITorneos['nombreTorneo']>;
  fechaInicio: FormControl<ITorneos['fechaInicio']>;
  fechaFin: FormControl<ITorneos['fechaFin']>;
  lugar: FormControl<ITorneos['lugar']>;
  tiempo: FormControl<ITorneos['tiempo']>;
  tipoTorneo: FormControl<ITorneos['tipoTorneo']>;
  torneoEvaluado: FormControl<ITorneos['torneoEvaluado']>;
  federado: FormControl<ITorneos['federado']>;
};

export type TorneosFormGroup = FormGroup<TorneosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TorneosFormService {
  createTorneosFormGroup(torneos: TorneosFormGroupInput = { id: null }): TorneosFormGroup {
    const torneosRawValue = {
      ...this.getFormDefaults(),
      ...torneos,
    };
    return new FormGroup<TorneosFormGroupContent>({
      id: new FormControl(
        { value: torneosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombreTorneo: new FormControl(torneosRawValue.nombreTorneo, {
        validators: [Validators.required],
      }),
      fechaInicio: new FormControl(torneosRawValue.fechaInicio, {
        validators: [Validators.required],
      }),
      fechaFin: new FormControl(torneosRawValue.fechaFin, {
        validators: [Validators.required],
      }),
      lugar: new FormControl(torneosRawValue.lugar, {
        validators: [Validators.required],
      }),
      tiempo: new FormControl(torneosRawValue.tiempo),
      tipoTorneo: new FormControl(torneosRawValue.tipoTorneo, {
        validators: [Validators.required],
      }),
      torneoEvaluado: new FormControl(torneosRawValue.torneoEvaluado, {
        validators: [Validators.required],
      }),
      federado: new FormControl(torneosRawValue.federado, {
        validators: [Validators.required],
      }),
    });
  }

  getTorneos(form: TorneosFormGroup): ITorneos | NewTorneos {
    return form.getRawValue() as ITorneos | NewTorneos;
  }

  resetForm(form: TorneosFormGroup, torneos: TorneosFormGroupInput): void {
    const torneosRawValue = { ...this.getFormDefaults(), ...torneos };
    form.reset(
      {
        ...torneosRawValue,
        id: { value: torneosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TorneosFormDefaults {
    return {
      id: null,
      torneoEvaluado: false,
      federado: false,
    };
  }
}
