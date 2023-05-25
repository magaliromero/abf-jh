import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPrestamos, NewPrestamos } from '../prestamos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrestamos for edit and NewPrestamosFormGroupInput for create.
 */
type PrestamosFormGroupInput = IPrestamos | PartialWithRequiredKeyOf<NewPrestamos>;

type PrestamosFormDefaults = Pick<NewPrestamos, 'id'>;

type PrestamosFormGroupContent = {
  id: FormControl<IPrestamos['id'] | NewPrestamos['id']>;
  fechaPrestamo: FormControl<IPrestamos['fechaPrestamo']>;
  fechaDevolucion: FormControl<IPrestamos['fechaDevolucion']>;
  estado: FormControl<IPrestamos['estado']>;
  materiales: FormControl<IPrestamos['materiales']>;
  alumnos: FormControl<IPrestamos['alumnos']>;
};

export type PrestamosFormGroup = FormGroup<PrestamosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PrestamosFormService {
  createPrestamosFormGroup(prestamos: PrestamosFormGroupInput = { id: null }): PrestamosFormGroup {
    const prestamosRawValue = {
      ...this.getFormDefaults(),
      ...prestamos,
    };
    return new FormGroup<PrestamosFormGroupContent>({
      id: new FormControl(
        { value: prestamosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fechaPrestamo: new FormControl(prestamosRawValue.fechaPrestamo, {
        validators: [Validators.required],
      }),
      fechaDevolucion: new FormControl(prestamosRawValue.fechaDevolucion),
      estado: new FormControl(prestamosRawValue.estado, {
        validators: [Validators.required],
      }),
      materiales: new FormControl(prestamosRawValue.materiales, {
        validators: [Validators.required],
      }),
      alumnos: new FormControl(prestamosRawValue.alumnos, {
        validators: [Validators.required],
      }),
    });
  }

  getPrestamos(form: PrestamosFormGroup): IPrestamos | NewPrestamos {
    return form.getRawValue() as IPrestamos | NewPrestamos;
  }

  resetForm(form: PrestamosFormGroup, prestamos: PrestamosFormGroupInput): void {
    const prestamosRawValue = { ...this.getFormDefaults(), ...prestamos };
    form.reset(
      {
        ...prestamosRawValue,
        id: { value: prestamosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PrestamosFormDefaults {
    return {
      id: null,
    };
  }
}
