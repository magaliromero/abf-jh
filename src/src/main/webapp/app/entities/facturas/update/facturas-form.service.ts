import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFacturas, NewFacturas } from '../facturas.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacturas for edit and NewFacturasFormGroupInput for create.
 */
type FacturasFormGroupInput = IFacturas | PartialWithRequiredKeyOf<NewFacturas>;

type FacturasFormDefaults = Pick<NewFacturas, 'id'>;

type FacturasFormGroupContent = {
  id: FormControl<IFacturas['id'] | NewFacturas['id']>;
  fecha: FormControl<IFacturas['fecha']>;
  facturaNro: FormControl<IFacturas['facturaNro']>;
  timbrado: FormControl<IFacturas['timbrado']>;
  razonSocial: FormControl<IFacturas['razonSocial']>;
  ruc: FormControl<IFacturas['ruc']>;
  condicionVenta: FormControl<IFacturas['condicionVenta']>;
  total: FormControl<IFacturas['total']>;
};

export type FacturasFormGroup = FormGroup<FacturasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FacturasFormService {
  createFacturasFormGroup(facturas: FacturasFormGroupInput = { id: null }): FacturasFormGroup {
    const facturasRawValue = {
      ...this.getFormDefaults(),
      ...facturas,
    };
    return new FormGroup<FacturasFormGroupContent>({
      id: new FormControl(
        { value: facturasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fecha: new FormControl(facturasRawValue.fecha, {
        validators: [Validators.required],
      }),
      facturaNro: new FormControl(facturasRawValue.facturaNro, {
        validators: [Validators.required],
      }),
      timbrado: new FormControl(facturasRawValue.timbrado, {
        validators: [Validators.required],
      }),
      razonSocial: new FormControl(facturasRawValue.razonSocial, {
        validators: [Validators.required],
      }),
      ruc: new FormControl(facturasRawValue.ruc, {
        validators: [Validators.required],
      }),
      condicionVenta: new FormControl(facturasRawValue.condicionVenta, {
        validators: [Validators.required],
      }),
      total: new FormControl(facturasRawValue.total, {
        validators: [Validators.required],
      }),
    });
  }

  getFacturas(form: FacturasFormGroup): IFacturas | NewFacturas {
    return form.getRawValue() as IFacturas | NewFacturas;
  }

  resetForm(form: FacturasFormGroup, facturas: FacturasFormGroupInput): void {
    const facturasRawValue = { ...this.getFormDefaults(), ...facturas };
    form.reset(
      {
        ...facturasRawValue,
        id: { value: facturasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FacturasFormDefaults {
    return {
      id: null,
    };
  }
}
