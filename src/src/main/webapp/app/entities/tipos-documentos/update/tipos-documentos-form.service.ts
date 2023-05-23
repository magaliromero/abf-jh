import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITiposDocumentos, NewTiposDocumentos } from '../tipos-documentos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITiposDocumentos for edit and NewTiposDocumentosFormGroupInput for create.
 */
type TiposDocumentosFormGroupInput = ITiposDocumentos | PartialWithRequiredKeyOf<NewTiposDocumentos>;

type TiposDocumentosFormDefaults = Pick<NewTiposDocumentos, 'id'>;

type TiposDocumentosFormGroupContent = {
  id: FormControl<ITiposDocumentos['id'] | NewTiposDocumentos['id']>;
  codigo: FormControl<ITiposDocumentos['codigo']>;
  descripcion: FormControl<ITiposDocumentos['descripcion']>;
};

export type TiposDocumentosFormGroup = FormGroup<TiposDocumentosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TiposDocumentosFormService {
  createTiposDocumentosFormGroup(tiposDocumentos: TiposDocumentosFormGroupInput = { id: null }): TiposDocumentosFormGroup {
    const tiposDocumentosRawValue = {
      ...this.getFormDefaults(),
      ...tiposDocumentos,
    };
    return new FormGroup<TiposDocumentosFormGroupContent>({
      id: new FormControl(
        { value: tiposDocumentosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codigo: new FormControl(tiposDocumentosRawValue.codigo, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(tiposDocumentosRawValue.descripcion, {
        validators: [Validators.required],
      }),
    });
  }

  getTiposDocumentos(form: TiposDocumentosFormGroup): ITiposDocumentos | NewTiposDocumentos {
    return form.getRawValue() as ITiposDocumentos | NewTiposDocumentos;
  }

  resetForm(form: TiposDocumentosFormGroup, tiposDocumentos: TiposDocumentosFormGroupInput): void {
    const tiposDocumentosRawValue = { ...this.getFormDefaults(), ...tiposDocumentos };
    form.reset(
      {
        ...tiposDocumentosRawValue,
        id: { value: tiposDocumentosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TiposDocumentosFormDefaults {
    return {
      id: null,
    };
  }
}
