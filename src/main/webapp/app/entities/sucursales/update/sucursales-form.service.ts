import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISucursales, NewSucursales } from '../sucursales.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISucursales for edit and NewSucursalesFormGroupInput for create.
 */
type SucursalesFormGroupInput = ISucursales | PartialWithRequiredKeyOf<NewSucursales>;

type SucursalesFormDefaults = Pick<NewSucursales, 'id'>;

type SucursalesFormGroupContent = {
  id: FormControl<ISucursales['id'] | NewSucursales['id']>;
  nombreSucursal: FormControl<ISucursales['nombreSucursal']>;
  direccion: FormControl<ISucursales['direccion']>;
  numeroEstablecimiento: FormControl<ISucursales['numeroEstablecimiento']>;
};

export type SucursalesFormGroup = FormGroup<SucursalesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SucursalesFormService {
  createSucursalesFormGroup(sucursales: SucursalesFormGroupInput = { id: null }): SucursalesFormGroup {
    const sucursalesRawValue = {
      ...this.getFormDefaults(),
      ...sucursales,
    };
    return new FormGroup<SucursalesFormGroupContent>({
      id: new FormControl(
        { value: sucursalesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombreSucursal: new FormControl(sucursalesRawValue.nombreSucursal, {
        validators: [Validators.required],
      }),
      direccion: new FormControl(sucursalesRawValue.direccion),
      numeroEstablecimiento: new FormControl(sucursalesRawValue.numeroEstablecimiento, {
        validators: [Validators.required],
      }),
    });
  }

  getSucursales(form: SucursalesFormGroup): ISucursales | NewSucursales {
    return form.getRawValue() as ISucursales | NewSucursales;
  }

  resetForm(form: SucursalesFormGroup, sucursales: SucursalesFormGroupInput): void {
    const sucursalesRawValue = { ...this.getFormDefaults(), ...sucursales };
    form.reset(
      {
        ...sucursalesRawValue,
        id: { value: sucursalesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SucursalesFormDefaults {
    return {
      id: null,
    };
  }
}
