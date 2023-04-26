import dayjs from 'dayjs/esm';

import { CondVenta } from 'app/entities/enumerations/cond-venta.model';

import { IFacturas, NewFacturas } from './facturas.model';

export const sampleWithRequiredData: IFacturas = {
  id: 29679,
  fecha: dayjs('2023-04-25'),
  facturaNro: 'a circuit',
  timbrado: 61190,
  ruc: 31438,
  descripcion: 'Account SMS Granito',
};

export const sampleWithPartialData: IFacturas = {
  id: 55207,
  fecha: dayjs('2023-04-25'),
  facturaNro: 'visualize vertical Salchichas',
  timbrado: 74568,
  ruc: 45930,
  condicionVenta: CondVenta['CONTADO'],
  descripcion: 'Opcional Calleja',
  valor5: 97576,
  valor10: 66134,
};

export const sampleWithFullData: IFacturas = {
  id: 13455,
  fecha: dayjs('2023-04-25'),
  facturaNro: 'compress bypassing',
  timbrado: 95856,
  ruc: 85696,
  condicionVenta: CondVenta['CREDITO'],
  cantidad: 76422,
  descripcion: 'killer optical',
  precioUnitario: 50026,
  valor5: 47683,
  valor10: 82815,
  total: 5477,
  total5: 78940,
  total10: 62152,
  totalIva: 27473,
};

export const sampleWithNewData: NewFacturas = {
  fecha: dayjs('2023-04-25'),
  facturaNro: 'Gambia',
  timbrado: 59500,
  ruc: 55248,
  descripcion: 'Borders Pescado Hormigon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
