import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRegistroClases, NewRegistroClases } from '../registro-clases.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRegistroClases for edit and NewRegistroClasesFormGroupInput for create.
 */
type RegistroClasesFormGroupInput = IRegistroClases | PartialWithRequiredKeyOf<NewRegistroClases>;

type RegistroClasesFormDefaults = Pick<NewRegistroClases, 'id' | 'asistenciaAlumno'>;

type RegistroClasesFormGroupContent = {
  id: FormControl<IRegistroClases['id'] | NewRegistroClases['id']>;
  fecha: FormControl<IRegistroClases['fecha']>;
  cantidadHoras: FormControl<IRegistroClases['cantidadHoras']>;
  asistenciaAlumno: FormControl<IRegistroClases['asistenciaAlumno']>;
  tema: FormControl<IRegistroClases['tema']>;
  funcionario: FormControl<IRegistroClases['funcionario']>;
};

export type RegistroClasesFormGroup = FormGroup<RegistroClasesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RegistroClasesFormService {
  createRegistroClasesFormGroup(registroClases: RegistroClasesFormGroupInput = { id: null }): RegistroClasesFormGroup {
    const registroClasesRawValue = {
      ...this.getFormDefaults(),
      ...registroClases,
    };
    return new FormGroup<RegistroClasesFormGroupContent>({
      id: new FormControl(
        { value: registroClasesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fecha: new FormControl(registroClasesRawValue.fecha, {
        validators: [Validators.required],
      }),
      cantidadHoras: new FormControl(registroClasesRawValue.cantidadHoras, {
        validators: [Validators.required],
      }),
      asistenciaAlumno: new FormControl(registroClasesRawValue.asistenciaAlumno),
      tema: new FormControl(registroClasesRawValue.tema, {
        validators: [Validators.required],
      }),
      funcionario: new FormControl(registroClasesRawValue.funcionario),
    });
  }

  getRegistroClases(form: RegistroClasesFormGroup): IRegistroClases | NewRegistroClases {
    return form.getRawValue() as IRegistroClases | NewRegistroClases;
  }

  resetForm(form: RegistroClasesFormGroup, registroClases: RegistroClasesFormGroupInput): void {
    const registroClasesRawValue = { ...this.getFormDefaults(), ...registroClases };
    form.reset(
      {
        ...registroClasesRawValue,
        id: { value: registroClasesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RegistroClasesFormDefaults {
    return {
      id: null,
      asistenciaAlumno: false,
    };
  }
}
