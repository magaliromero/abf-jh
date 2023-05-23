import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAlumnos, NewAlumnos } from '../alumnos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlumnos for edit and NewAlumnosFormGroupInput for create.
 */
type AlumnosFormGroupInput = IAlumnos | PartialWithRequiredKeyOf<NewAlumnos>;

type AlumnosFormDefaults = Pick<NewAlumnos, 'id'>;

type AlumnosFormGroupContent = {
  id: FormControl<IAlumnos['id'] | NewAlumnos['id']>;
  elo: FormControl<IAlumnos['elo']>;
  fideId: FormControl<IAlumnos['fideId']>;
  nombres: FormControl<IAlumnos['nombres']>;
  apellidos: FormControl<IAlumnos['apellidos']>;
  nombreCompleto: FormControl<IAlumnos['nombreCompleto']>;
  email: FormControl<IAlumnos['email']>;
  telefono: FormControl<IAlumnos['telefono']>;
  fechaNacimiento: FormControl<IAlumnos['fechaNacimiento']>;
  documento: FormControl<IAlumnos['documento']>;
  estado: FormControl<IAlumnos['estado']>;
  tipoDocumentos: FormControl<IAlumnos['tipoDocumentos']>;
};

export type AlumnosFormGroup = FormGroup<AlumnosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlumnosFormService {
  createAlumnosFormGroup(alumnos: AlumnosFormGroupInput = { id: null }): AlumnosFormGroup {
    const alumnosRawValue = {
      ...this.getFormDefaults(),
      ...alumnos,
    };
    return new FormGroup<AlumnosFormGroupContent>({
      id: new FormControl(
        { value: alumnosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      elo: new FormControl(alumnosRawValue.elo),
      fideId: new FormControl(alumnosRawValue.fideId),
      nombres: new FormControl(alumnosRawValue.nombres, {
        validators: [Validators.required],
      }),
      apellidos: new FormControl(alumnosRawValue.apellidos, {
        validators: [Validators.required],
      }),
      nombreCompleto: new FormControl(alumnosRawValue.nombreCompleto, {
        validators: [Validators.required],
      }),
      email: new FormControl(alumnosRawValue.email),
      telefono: new FormControl(alumnosRawValue.telefono, {
        validators: [Validators.required],
      }),
      fechaNacimiento: new FormControl(alumnosRawValue.fechaNacimiento, {
        validators: [Validators.required],
      }),
      documento: new FormControl(alumnosRawValue.documento, {
        validators: [Validators.required],
      }),
      estado: new FormControl(alumnosRawValue.estado, {
        validators: [Validators.required],
      }),
      tipoDocumentos: new FormControl(alumnosRawValue.tipoDocumentos, {
        validators: [Validators.required],
      }),
    });
  }

  getAlumnos(form: AlumnosFormGroup): IAlumnos | NewAlumnos {
    return form.getRawValue() as IAlumnos | NewAlumnos;
  }

  resetForm(form: AlumnosFormGroup, alumnos: AlumnosFormGroupInput): void {
    const alumnosRawValue = { ...this.getFormDefaults(), ...alumnos };
    form.reset(
      {
        ...alumnosRawValue,
        id: { value: alumnosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AlumnosFormDefaults {
    return {
      id: null,
    };
  }
}
