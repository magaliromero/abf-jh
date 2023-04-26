import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICobros, NewCobros } from '../cobros.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICobros for edit and NewCobrosFormGroupInput for create.
 */
type CobrosFormGroupInput = ICobros | PartialWithRequiredKeyOf<NewCobros>;

type CobrosFormDefaults = Pick<NewCobros, 'id'>;

type CobrosFormGroupContent = {
  id: FormControl<ICobros['id'] | NewCobros['id']>;
  montoPago: FormControl<ICobros['montoPago']>;
  montoInicial: FormControl<ICobros['montoInicial']>;
  saldo: FormControl<ICobros['saldo']>;
  fechaRegistro: FormControl<ICobros['fechaRegistro']>;
  fechaPago: FormControl<ICobros['fechaPago']>;
  tipoPago: FormControl<ICobros['tipoPago']>;
  descripcion: FormControl<ICobros['descripcion']>;
  alumnos: FormControl<ICobros['alumnos']>;
  factura: FormControl<ICobros['factura']>;
};

export type CobrosFormGroup = FormGroup<CobrosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CobrosFormService {
  createCobrosFormGroup(cobros: CobrosFormGroupInput = { id: null }): CobrosFormGroup {
    const cobrosRawValue = {
      ...this.getFormDefaults(),
      ...cobros,
    };
    return new FormGroup<CobrosFormGroupContent>({
      id: new FormControl(
        { value: cobrosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      montoPago: new FormControl(cobrosRawValue.montoPago, {
        validators: [Validators.required],
      }),
      montoInicial: new FormControl(cobrosRawValue.montoInicial, {
        validators: [Validators.required],
      }),
      saldo: new FormControl(cobrosRawValue.saldo, {
        validators: [Validators.required],
      }),
      fechaRegistro: new FormControl(cobrosRawValue.fechaRegistro, {
        validators: [Validators.required],
      }),
      fechaPago: new FormControl(cobrosRawValue.fechaPago, {
        validators: [Validators.required],
      }),
      tipoPago: new FormControl(cobrosRawValue.tipoPago, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(cobrosRawValue.descripcion, {
        validators: [Validators.required],
      }),
      alumnos: new FormControl(cobrosRawValue.alumnos, {
        validators: [Validators.required],
      }),
      factura: new FormControl(cobrosRawValue.factura, {
        validators: [Validators.required],
      }),
    });
  }

  getCobros(form: CobrosFormGroup): ICobros | NewCobros {
    return form.getRawValue() as ICobros | NewCobros;
  }

  resetForm(form: CobrosFormGroup, cobros: CobrosFormGroupInput): void {
    const cobrosRawValue = { ...this.getFormDefaults(), ...cobros };
    form.reset(
      {
        ...cobrosRawValue,
        id: { value: cobrosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CobrosFormDefaults {
    return {
      id: null,
    };
  }
}
