import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEvaluaciones, NewEvaluaciones } from '../evaluaciones.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvaluaciones for edit and NewEvaluacionesFormGroupInput for create.
 */
type EvaluacionesFormGroupInput = IEvaluaciones | PartialWithRequiredKeyOf<NewEvaluaciones>;

type EvaluacionesFormDefaults = Pick<NewEvaluaciones, 'id'>;

type EvaluacionesFormGroupContent = {
  id: FormControl<IEvaluaciones['id'] | NewEvaluaciones['id']>;
  nroEvaluacion: FormControl<IEvaluaciones['nroEvaluacion']>;
  fecha: FormControl<IEvaluaciones['fecha']>;
  alumnos: FormControl<IEvaluaciones['alumnos']>;
  funcionarios: FormControl<IEvaluaciones['funcionarios']>;
};

export type EvaluacionesFormGroup = FormGroup<EvaluacionesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvaluacionesFormService {
  createEvaluacionesFormGroup(evaluaciones: EvaluacionesFormGroupInput = { id: null }): EvaluacionesFormGroup {
    const evaluacionesRawValue = {
      ...this.getFormDefaults(),
      ...evaluaciones,
    };
    return new FormGroup<EvaluacionesFormGroupContent>({
      id: new FormControl(
        { value: evaluacionesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nroEvaluacion: new FormControl(evaluacionesRawValue.nroEvaluacion, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(evaluacionesRawValue.fecha, {
        validators: [Validators.required],
      }),
      alumnos: new FormControl(evaluacionesRawValue.alumnos, {
        validators: [Validators.required],
      }),
      funcionarios: new FormControl(evaluacionesRawValue.funcionarios, {
        validators: [Validators.required],
      }),
    });
  }

  getEvaluaciones(form: EvaluacionesFormGroup): IEvaluaciones | NewEvaluaciones {
    return form.getRawValue() as IEvaluaciones | NewEvaluaciones;
  }

  resetForm(form: EvaluacionesFormGroup, evaluaciones: EvaluacionesFormGroupInput): void {
    const evaluacionesRawValue = { ...this.getFormDefaults(), ...evaluaciones };
    form.reset(
      {
        ...evaluacionesRawValue,
        id: { value: evaluacionesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EvaluacionesFormDefaults {
    return {
      id: null,
    };
  }
}
