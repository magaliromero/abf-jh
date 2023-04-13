import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPagos, NewPagos } from '../pagos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPagos for edit and NewPagosFormGroupInput for create.
 */
type PagosFormGroupInput = IPagos | PartialWithRequiredKeyOf<NewPagos>;

type PagosFormDefaults = Pick<NewPagos, 'id'>;

type PagosFormGroupContent = {
  id: FormControl<IPagos['id'] | NewPagos['id']>;
  montoPago: FormControl<IPagos['montoPago']>;
  montoInicial: FormControl<IPagos['montoInicial']>;
  saldo: FormControl<IPagos['saldo']>;
  fechaRegistro: FormControl<IPagos['fechaRegistro']>;
  fechaPago: FormControl<IPagos['fechaPago']>;
  tipoPago: FormControl<IPagos['tipoPago']>;
  descripcion: FormControl<IPagos['descripcion']>;
  funcionarios: FormControl<IPagos['funcionarios']>;
};

export type PagosFormGroup = FormGroup<PagosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PagosFormService {
  createPagosFormGroup(pagos: PagosFormGroupInput = { id: null }): PagosFormGroup {
    const pagosRawValue = {
      ...this.getFormDefaults(),
      ...pagos,
    };
    return new FormGroup<PagosFormGroupContent>({
      id: new FormControl(
        { value: pagosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      montoPago: new FormControl(pagosRawValue.montoPago, {
        validators: [Validators.required],
      }),
      montoInicial: new FormControl(pagosRawValue.montoInicial, {
        validators: [Validators.required],
      }),
      saldo: new FormControl(pagosRawValue.saldo, {
        validators: [Validators.required],
      }),
      fechaRegistro: new FormControl(pagosRawValue.fechaRegistro, {
        validators: [Validators.required],
      }),
      fechaPago: new FormControl(pagosRawValue.fechaPago, {
        validators: [Validators.required],
      }),
      tipoPago: new FormControl(pagosRawValue.tipoPago, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(pagosRawValue.descripcion, {
        validators: [Validators.required],
      }),
      funcionarios: new FormControl(pagosRawValue.funcionarios, {
        validators: [Validators.required],
      }),
    });
  }

  getPagos(form: PagosFormGroup): IPagos | NewPagos {
    return form.getRawValue() as IPagos | NewPagos;
  }

  resetForm(form: PagosFormGroup, pagos: PagosFormGroupInput): void {
    const pagosRawValue = { ...this.getFormDefaults(), ...pagos };
    form.reset(
      {
        ...pagosRawValue,
        id: { value: pagosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PagosFormDefaults {
    return {
      id: null,
    };
  }
}
