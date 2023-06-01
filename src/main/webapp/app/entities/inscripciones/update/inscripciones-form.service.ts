import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInscripciones, NewInscripciones } from '../inscripciones.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInscripciones for edit and NewInscripcionesFormGroupInput for create.
 */
type InscripcionesFormGroupInput = IInscripciones | PartialWithRequiredKeyOf<NewInscripciones>;

type InscripcionesFormDefaults = Pick<NewInscripciones, 'id'>;

type InscripcionesFormGroupContent = {
  id: FormControl<IInscripciones['id'] | NewInscripciones['id']>;
  fechaInscripcion: FormControl<IInscripciones['fechaInscripcion']>;
  alumnos: FormControl<IInscripciones['alumnos']>;
  cursos: FormControl<IInscripciones['cursos']>;
};

export type InscripcionesFormGroup = FormGroup<InscripcionesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InscripcionesFormService {
  createInscripcionesFormGroup(inscripciones: InscripcionesFormGroupInput = { id: null }): InscripcionesFormGroup {
    const inscripcionesRawValue = {
      ...this.getFormDefaults(),
      ...inscripciones,
    };
    return new FormGroup<InscripcionesFormGroupContent>({
      id: new FormControl(
        { value: inscripcionesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fechaInscripcion: new FormControl(inscripcionesRawValue.fechaInscripcion),
      alumnos: new FormControl(inscripcionesRawValue.alumnos, {
        validators: [Validators.required],
      }),
      cursos: new FormControl(inscripcionesRawValue.cursos, {
        validators: [Validators.required],
      }),
    });
  }

  getInscripciones(form: InscripcionesFormGroup): IInscripciones | NewInscripciones {
    return form.getRawValue() as IInscripciones | NewInscripciones;
  }

  resetForm(form: InscripcionesFormGroup, inscripciones: InscripcionesFormGroupInput): void {
    const inscripcionesRawValue = { ...this.getFormDefaults(), ...inscripciones };
    form.reset(
      {
        ...inscripcionesRawValue,
        id: { value: inscripcionesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InscripcionesFormDefaults {
    return {
      id: null,
    };
  }
}
