import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICursos, NewCursos } from '../cursos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICursos for edit and NewCursosFormGroupInput for create.
 */
type CursosFormGroupInput = ICursos | PartialWithRequiredKeyOf<NewCursos>;

type CursosFormDefaults = Pick<NewCursos, 'id'>;

type CursosFormGroupContent = {
  id: FormControl<ICursos['id'] | NewCursos['id']>;
  nombreCurso: FormControl<ICursos['nombreCurso']>;
  descripcion: FormControl<ICursos['descripcion']>;
  fechaInicio: FormControl<ICursos['fechaInicio']>;
  fechaFin: FormControl<ICursos['fechaFin']>;
  cantidadClases: FormControl<ICursos['cantidadClases']>;
  nivel: FormControl<ICursos['nivel']>;
  temas: FormControl<ICursos['temas']>;
};

export type CursosFormGroup = FormGroup<CursosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CursosFormService {
  createCursosFormGroup(cursos: CursosFormGroupInput = { id: null }): CursosFormGroup {
    const cursosRawValue = {
      ...this.getFormDefaults(),
      ...cursos,
    };
    return new FormGroup<CursosFormGroupContent>({
      id: new FormControl(
        { value: cursosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombreCurso: new FormControl(cursosRawValue.nombreCurso, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(cursosRawValue.descripcion, {
        validators: [Validators.required],
      }),
      fechaInicio: new FormControl(cursosRawValue.fechaInicio),
      fechaFin: new FormControl(cursosRawValue.fechaFin),
      cantidadClases: new FormControl(cursosRawValue.cantidadClases),
      nivel: new FormControl(cursosRawValue.nivel, {
        validators: [Validators.required],
      }),
      temas: new FormControl(cursosRawValue.temas, {
        validators: [Validators.required],
      }),
    });
  }

  getCursos(form: CursosFormGroup): ICursos | NewCursos {
    return form.getRawValue() as ICursos | NewCursos;
  }

  resetForm(form: CursosFormGroup, cursos: CursosFormGroupInput): void {
    const cursosRawValue = { ...this.getFormDefaults(), ...cursos };
    form.reset(
      {
        ...cursosRawValue,
        id: { value: cursosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CursosFormDefaults {
    return {
      id: null,
    };
  }
}
