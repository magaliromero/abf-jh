import dayjs from 'dayjs/esm';

import { CondVenta } from 'app/entities/enumerations/cond-venta.model';

import { IFacturas, NewFacturas } from './facturas.model';

export const sampleWithRequiredData: IFacturas = {
  id: 29679,
  fecha: dayjs('2023-04-25'),
  facturaNro: 'a circuit',
  timbrado: 61190,
  razonSocial: 'Zealand',
  ruc: 91251,
  descripcion: 'Solomon Extremadura usuario',
};

export const sampleWithPartialData: IFacturas = {
  id: 69558,
  fecha: dayjs('2023-04-25'),
  facturaNro: 'Sabroso Contabilidad',
  timbrado: 45930,
  razonSocial: 'Dinamarca Distrito',
  ruc: 3999,
  condicionVenta: CondVenta['CREDITO'],
  descripcion: 'Hormigon compress',
  total: 84802,
  total10: 8304,
};

export const sampleWithFullData: IFacturas = {
  id: 37741,
  fecha: dayjs('2023-04-25'),
  facturaNro: 'input killer optical',
  timbrado: 50026,
  razonSocial: 'up Money',
  ruc: 15559,
  condicionVenta: CondVenta['CREDITO'],
  cantidad: 33469,
  descripcion: 'Account Borders',
  precioUnitario: 6566,
  valor5: 96098,
  valor10: 66723,
  total: 71662,
  total5: 67821,
  total10: 11173,
  totalIva: 91785,
};

export const sampleWithNewData: NewFacturas = {
  fecha: dayjs('2023-04-25'),
  facturaNro: 'Distribuido embrace',
  timbrado: 47775,
  razonSocial: 'driver applications Bricolaje',
  ruc: 53729,
  descripcion: 'interfaces Pequeño matrix',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
