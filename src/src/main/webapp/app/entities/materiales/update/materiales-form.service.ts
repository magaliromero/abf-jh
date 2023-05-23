import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMateriales, NewMateriales } from '../materiales.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMateriales for edit and NewMaterialesFormGroupInput for create.
 */
type MaterialesFormGroupInput = IMateriales | PartialWithRequiredKeyOf<NewMateriales>;

type MaterialesFormDefaults = Pick<NewMateriales, 'id'>;

type MaterialesFormGroupContent = {
  id: FormControl<IMateriales['id'] | NewMateriales['id']>;
  descripcion: FormControl<IMateriales['descripcion']>;
  cantidad: FormControl<IMateriales['cantidad']>;
  cantidadEnPrestamo: FormControl<IMateriales['cantidadEnPrestamo']>;
};

export type MaterialesFormGroup = FormGroup<MaterialesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaterialesFormService {
  createMaterialesFormGroup(materiales: MaterialesFormGroupInput = { id: null }): MaterialesFormGroup {
    const materialesRawValue = {
      ...this.getFormDefaults(),
      ...materiales,
    };
    return new FormGroup<MaterialesFormGroupContent>({
      id: new FormControl(
        { value: materialesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      descripcion: new FormControl(materialesRawValue.descripcion, {
        validators: [Validators.required],
      }),
      cantidad: new FormControl(materialesRawValue.cantidad),
      cantidadEnPrestamo: new FormControl(materialesRawValue.cantidadEnPrestamo),
    });
  }

  getMateriales(form: MaterialesFormGroup): IMateriales | NewMateriales {
    return form.getRawValue() as IMateriales | NewMateriales;
  }

  resetForm(form: MaterialesFormGroup, materiales: MaterialesFormGroupInput): void {
    const materialesRawValue = { ...this.getFormDefaults(), ...materiales };
    form.reset(
      {
        ...materialesRawValue,
        id: { value: materialesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MaterialesFormDefaults {
    return {
      id: null,
    };
  }
}
