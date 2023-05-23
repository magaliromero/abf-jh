import { IFacturaDetalle, NewFacturaDetalle } from './factura-detalle.model';

export const sampleWithRequiredData: IFacturaDetalle = {
  id: 75937,
};

export const sampleWithPartialData: IFacturaDetalle = {
  id: 28221,
  subtotal: 44237,
  porcentajeIva: 29590,
};

export const sampleWithFullData: IFacturaDetalle = {
  id: 28908,
  cantidad: 95357,
  precioUnitario: 18337,
  subtotal: 2938,
  porcentajeIva: 93147,
  valorPorcentaje: 97646,
};

export const sampleWithNewData: NewFacturaDetalle = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
