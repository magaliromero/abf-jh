import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEvaluacionesDetalle, NewEvaluacionesDetalle } from '../evaluaciones-detalle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvaluacionesDetalle for edit and NewEvaluacionesDetalleFormGroupInput for create.
 */
type EvaluacionesDetalleFormGroupInput = IEvaluacionesDetalle | PartialWithRequiredKeyOf<NewEvaluacionesDetalle>;

type EvaluacionesDetalleFormDefaults = Pick<NewEvaluacionesDetalle, 'id'>;

type EvaluacionesDetalleFormGroupContent = {
  id: FormControl<IEvaluacionesDetalle['id'] | NewEvaluacionesDetalle['id']>;
  comentarios: FormControl<IEvaluacionesDetalle['comentarios']>;
  puntaje: FormControl<IEvaluacionesDetalle['puntaje']>;
  evaluaciones: FormControl<IEvaluacionesDetalle['evaluaciones']>;
  temas: FormControl<IEvaluacionesDetalle['temas']>;
};

export type EvaluacionesDetalleFormGroup = FormGroup<EvaluacionesDetalleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvaluacionesDetalleFormService {
  createEvaluacionesDetalleFormGroup(evaluacionesDetalle: EvaluacionesDetalleFormGroupInput = { id: null }): EvaluacionesDetalleFormGroup {
    const evaluacionesDetalleRawValue = {
      ...this.getFormDefaults(),
      ...evaluacionesDetalle,
    };
    return new FormGroup<EvaluacionesDetalleFormGroupContent>({
      id: new FormControl(
        { value: evaluacionesDetalleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      comentarios: new FormControl(evaluacionesDetalleRawValue.comentarios, {
        validators: [Validators.required],
      }),
      puntaje: new FormControl(evaluacionesDetalleRawValue.puntaje, {
        validators: [Validators.required],
      }),
      evaluaciones: new FormControl(evaluacionesDetalleRawValue.evaluaciones, {
        validators: [Validators.required],
      }),
      temas: new FormControl(evaluacionesDetalleRawValue.temas, {
        validators: [Validators.required],
      }),
    });
  }

  getEvaluacionesDetalle(form: EvaluacionesDetalleFormGroup): IEvaluacionesDetalle | NewEvaluacionesDetalle {
    return form.getRawValue() as IEvaluacionesDetalle | NewEvaluacionesDetalle;
  }

  resetForm(form: EvaluacionesDetalleFormGroup, evaluacionesDetalle: EvaluacionesDetalleFormGroupInput): void {
    const evaluacionesDetalleRawValue = { ...this.getFormDefaults(), ...evaluacionesDetalle };
    form.reset(
      {
        ...evaluacionesDetalleRawValue,
        id: { value: evaluacionesDetalleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EvaluacionesDetalleFormDefaults {
    return {
      id: null,
    };
  }
}
