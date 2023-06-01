import { TipoProductos } from 'app/entities/enumerations/tipo-productos.model';

import { IProductos, NewProductos } from './productos.model';

export const sampleWithRequiredData: IProductos = {
  id: 57594,
  tipoProducto: TipoProductos['SERVICIO'],
  precioUnitario: 4500,
  porcentajeIva: 3613,
  descripcion: 'Director Metal neural',
};

export const sampleWithPartialData: IProductos = {
  id: 96089,
  tipoProducto: TipoProductos['SERVICIO'],
  precioUnitario: 49319,
  porcentajeIva: 24521,
  descripcion: 'Buckinghamshire invoice',
};

export const sampleWithFullData: IProductos = {
  id: 45427,
  tipoProducto: TipoProductos['PRODUCTO'],
  precioUnitario: 553,
  porcentajeIva: 4120,
  descripcion: 'Minnesota',
};

export const sampleWithNewData: NewProductos = {
  tipoProducto: TipoProductos['SERVICIO'],
  precioUnitario: 35076,
  porcentajeIva: 31870,
  descripcion: 'asynchronous Persistent',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
