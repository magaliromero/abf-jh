import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFichaPartidasTorneos, NewFichaPartidasTorneos } from '../ficha-partidas-torneos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFichaPartidasTorneos for edit and NewFichaPartidasTorneosFormGroupInput for create.
 */
type FichaPartidasTorneosFormGroupInput = IFichaPartidasTorneos | PartialWithRequiredKeyOf<NewFichaPartidasTorneos>;

type FichaPartidasTorneosFormDefaults = Pick<NewFichaPartidasTorneos, 'id'>;

type FichaPartidasTorneosFormGroupContent = {
  id: FormControl<IFichaPartidasTorneos['id'] | NewFichaPartidasTorneos['id']>;
  nombreContrincante: FormControl<IFichaPartidasTorneos['nombreContrincante']>;
  duracion: FormControl<IFichaPartidasTorneos['duracion']>;
  winner: FormControl<IFichaPartidasTorneos['winner']>;
  resultado: FormControl<IFichaPartidasTorneos['resultado']>;
  comentarios: FormControl<IFichaPartidasTorneos['comentarios']>;
  nombreArbitro: FormControl<IFichaPartidasTorneos['nombreArbitro']>;
  torneos: FormControl<IFichaPartidasTorneos['torneos']>;
  alumnos: FormControl<IFichaPartidasTorneos['alumnos']>;
};

export type FichaPartidasTorneosFormGroup = FormGroup<FichaPartidasTorneosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FichaPartidasTorneosFormService {
  createFichaPartidasTorneosFormGroup(
    fichaPartidasTorneos: FichaPartidasTorneosFormGroupInput = { id: null }
  ): FichaPartidasTorneosFormGroup {
    const fichaPartidasTorneosRawValue = {
      ...this.getFormDefaults(),
      ...fichaPartidasTorneos,
    };
    return new FormGroup<FichaPartidasTorneosFormGroupContent>({
      id: new FormControl(
        { value: fichaPartidasTorneosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombreContrincante: new FormControl(fichaPartidasTorneosRawValue.nombreContrincante),
      duracion: new FormControl(fichaPartidasTorneosRawValue.duracion),
      winner: new FormControl(fichaPartidasTorneosRawValue.winner),
      resultado: new FormControl(fichaPartidasTorneosRawValue.resultado),
      comentarios: new FormControl(fichaPartidasTorneosRawValue.comentarios),
      nombreArbitro: new FormControl(fichaPartidasTorneosRawValue.nombreArbitro),
      torneos: new FormControl(fichaPartidasTorneosRawValue.torneos, {
        validators: [Validators.required],
      }),
      alumnos: new FormControl(fichaPartidasTorneosRawValue.alumnos, {
        validators: [Validators.required],
      }),
    });
  }

  getFichaPartidasTorneos(form: FichaPartidasTorneosFormGroup): IFichaPartidasTorneos | NewFichaPartidasTorneos {
    return form.getRawValue() as IFichaPartidasTorneos | NewFichaPartidasTorneos;
  }

  resetForm(form: FichaPartidasTorneosFormGroup, fichaPartidasTorneos: FichaPartidasTorneosFormGroupInput): void {
    const fichaPartidasTorneosRawValue = { ...this.getFormDefaults(), ...fichaPartidasTorneos };
    form.reset(
      {
        ...fichaPartidasTorneosRawValue,
        id: { value: fichaPartidasTorneosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FichaPartidasTorneosFormDefaults {
    return {
      id: null,
    };
  }
}
