import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITimbrados, NewTimbrados } from '../timbrados.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITimbrados for edit and NewTimbradosFormGroupInput for create.
 */
type TimbradosFormGroupInput = ITimbrados | PartialWithRequiredKeyOf<NewTimbrados>;

type TimbradosFormDefaults = Pick<NewTimbrados, 'id'>;

type TimbradosFormGroupContent = {
  id: FormControl<ITimbrados['id'] | NewTimbrados['id']>;
  numeroTimbrado: FormControl<ITimbrados['numeroTimbrado']>;
  fechaInicio: FormControl<ITimbrados['fechaInicio']>;
  fechaFin: FormControl<ITimbrados['fechaFin']>;
  sucursales: FormControl<ITimbrados['sucursales']>;
};

export type TimbradosFormGroup = FormGroup<TimbradosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TimbradosFormService {
  createTimbradosFormGroup(timbrados: TimbradosFormGroupInput = { id: null }): TimbradosFormGroup {
    const timbradosRawValue = {
      ...this.getFormDefaults(),
      ...timbrados,
    };
    return new FormGroup<TimbradosFormGroupContent>({
      id: new FormControl(
        { value: timbradosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      numeroTimbrado: new FormControl(timbradosRawValue.numeroTimbrado, {
        validators: [Validators.required],
      }),
      fechaInicio: new FormControl(timbradosRawValue.fechaInicio, {
        validators: [Validators.required],
      }),
      fechaFin: new FormControl(timbradosRawValue.fechaFin, {
        validators: [Validators.required],
      }),
      sucursales: new FormControl(timbradosRawValue.sucursales),
    });
  }

  getTimbrados(form: TimbradosFormGroup): ITimbrados | NewTimbrados {
    return form.getRawValue() as ITimbrados | NewTimbrados;
  }

  resetForm(form: TimbradosFormGroup, timbrados: TimbradosFormGroupInput): void {
    const timbradosRawValue = { ...this.getFormDefaults(), ...timbrados };
    form.reset(
      {
        ...timbradosRawValue,
        id: { value: timbradosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TimbradosFormDefaults {
    return {
      id: null,
    };
  }
}
