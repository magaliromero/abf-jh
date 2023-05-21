import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFacturaDetalle, NewFacturaDetalle } from '../factura-detalle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFacturaDetalle for edit and NewFacturaDetalleFormGroupInput for create.
 */
type FacturaDetalleFormGroupInput = IFacturaDetalle | PartialWithRequiredKeyOf<NewFacturaDetalle>;

type FacturaDetalleFormDefaults = Pick<NewFacturaDetalle, 'id'>;

type FacturaDetalleFormGroupContent = {
  id: FormControl<IFacturaDetalle['id'] | NewFacturaDetalle['id']>;
  cantidad: FormControl<IFacturaDetalle['cantidad']>;
  precioUnitario: FormControl<IFacturaDetalle['precioUnitario']>;
  subtotal: FormControl<IFacturaDetalle['subtotal']>;
  porcentajeIva: FormControl<IFacturaDetalle['porcentajeIva']>;
  valorPorcentaje: FormControl<IFacturaDetalle['valorPorcentaje']>;
  producto: FormControl<IFacturaDetalle['producto']>;
  factura: FormControl<IFacturaDetalle['factura']>;
};

export type FacturaDetalleFormGroup = FormGroup<FacturaDetalleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FacturaDetalleFormService {
  createFacturaDetalleFormGroup(facturaDetalle: FacturaDetalleFormGroupInput = { id: null }): FacturaDetalleFormGroup {
    const facturaDetalleRawValue = {
      ...this.getFormDefaults(),
      ...facturaDetalle,
    };
    return new FormGroup<FacturaDetalleFormGroupContent>({
      id: new FormControl(
        { value: facturaDetalleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      cantidad: new FormControl(facturaDetalleRawValue.cantidad),
      precioUnitario: new FormControl(facturaDetalleRawValue.precioUnitario),
      subtotal: new FormControl(facturaDetalleRawValue.subtotal),
      porcentajeIva: new FormControl(facturaDetalleRawValue.porcentajeIva),
      valorPorcentaje: new FormControl(facturaDetalleRawValue.valorPorcentaje),
      producto: new FormControl(facturaDetalleRawValue.producto, {
        validators: [Validators.required],
      }),
      factura: new FormControl(facturaDetalleRawValue.factura, {
        validators: [Validators.required],
      }),
    });
  }

  getFacturaDetalle(form: FacturaDetalleFormGroup): IFacturaDetalle | NewFacturaDetalle {
    return form.getRawValue() as IFacturaDetalle | NewFacturaDetalle;
  }

  resetForm(form: FacturaDetalleFormGroup, facturaDetalle: FacturaDetalleFormGroupInput): void {
    const facturaDetalleRawValue = { ...this.getFormDefaults(), ...facturaDetalle };
    form.reset(
      {
        ...facturaDetalleRawValue,
        id: { value: facturaDetalleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FacturaDetalleFormDefaults {
    return {
      id: null,
    };
  }
}
