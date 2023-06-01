import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITemas, NewTemas } from '../temas.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITemas for edit and NewTemasFormGroupInput for create.
 */
type TemasFormGroupInput = ITemas | PartialWithRequiredKeyOf<NewTemas>;

type TemasFormDefaults = Pick<NewTemas, 'id'>;

type TemasFormGroupContent = {
  id: FormControl<ITemas['id'] | NewTemas['id']>;
  titulo: FormControl<ITemas['titulo']>;
  descripcion: FormControl<ITemas['descripcion']>;
  cursos: FormControl<ITemas['cursos']>;
};

export type TemasFormGroup = FormGroup<TemasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TemasFormService {
  createTemasFormGroup(temas: TemasFormGroupInput = { id: null }): TemasFormGroup {
    const temasRawValue = {
      ...this.getFormDefaults(),
      ...temas,
    };
    return new FormGroup<TemasFormGroupContent>({
      id: new FormControl(
        { value: temasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      titulo: new FormControl(temasRawValue.titulo, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(temasRawValue.descripcion, {
        validators: [Validators.required],
      }),
      cursos: new FormControl(temasRawValue.cursos, {
        validators: [Validators.required],
      }),
    });
  }

  getTemas(form: TemasFormGroup): ITemas | NewTemas {
    return form.getRawValue() as ITemas | NewTemas;
  }

  resetForm(form: TemasFormGroup, temas: TemasFormGroupInput): void {
    const temasRawValue = { ...this.getFormDefaults(), ...temas };
    form.reset(
      {
        ...temasRawValue,
        id: { value: temasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TemasFormDefaults {
    return {
      id: null,
    };
  }
}
