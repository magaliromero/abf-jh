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
  fecha: FormControl<IPagos['fecha']>;
  total: FormControl<IPagos['total']>;
  cantidadHoras: FormControl<IPagos['cantidadHoras']>;
  producto: FormControl<IPagos['producto']>;
  funcionario: FormControl<IPagos['funcionario']>;
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
      fecha: new FormControl(pagosRawValue.fecha, {
        validators: [Validators.required],
      }),
      total: new FormControl(pagosRawValue.total, {
        validators: [Validators.required],
      }),
      cantidadHoras: new FormControl(pagosRawValue.cantidadHoras, {
        validators: [Validators.required],
      }),
      producto: new FormControl(pagosRawValue.producto, {
        validators: [Validators.required],
      }),
      funcionario: new FormControl(pagosRawValue.funcionario),
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
