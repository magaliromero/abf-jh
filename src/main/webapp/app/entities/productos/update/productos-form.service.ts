import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductos, NewProductos } from '../productos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductos for edit and NewProductosFormGroupInput for create.
 */
type ProductosFormGroupInput = IProductos | PartialWithRequiredKeyOf<NewProductos>;

type ProductosFormDefaults = Pick<NewProductos, 'id'>;

type ProductosFormGroupContent = {
  id: FormControl<IProductos['id'] | NewProductos['id']>;
  tipoProducto: FormControl<IProductos['tipoProducto']>;
  precioUnitario: FormControl<IProductos['precioUnitario']>;
  porcentajeIva: FormControl<IProductos['porcentajeIva']>;
  descripcion: FormControl<IProductos['descripcion']>;
};

export type ProductosFormGroup = FormGroup<ProductosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductosFormService {
  createProductosFormGroup(productos: ProductosFormGroupInput = { id: null }): ProductosFormGroup {
    const productosRawValue = {
      ...this.getFormDefaults(),
      ...productos,
    };
    return new FormGroup<ProductosFormGroupContent>({
      id: new FormControl(
        { value: productosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tipoProducto: new FormControl(productosRawValue.tipoProducto, {
        validators: [Validators.required],
      }),
      precioUnitario: new FormControl(productosRawValue.precioUnitario, {
        validators: [Validators.required],
      }),
      porcentajeIva: new FormControl(productosRawValue.porcentajeIva, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(productosRawValue.descripcion, {
        validators: [Validators.required],
      }),
    });
  }

  getProductos(form: ProductosFormGroup): IProductos | NewProductos {
    return form.getRawValue() as IProductos | NewProductos;
  }

  resetForm(form: ProductosFormGroup, productos: ProductosFormGroupInput): void {
    const productosRawValue = { ...this.getFormDefaults(), ...productos };
    form.reset(
      {
        ...productosRawValue,
        id: { value: productosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductosFormDefaults {
    return {
      id: null,
    };
  }
}
