import dayjs from 'dayjs/esm';

import { TiposPagos } from 'app/entities/enumerations/tipos-pagos.model';

import { IPagos, NewPagos } from './pagos.model';

export const sampleWithRequiredData: IPagos = {
  id: 35750,
  montoPago: 69414,
  montoInicial: 36645,
  saldo: 2075,
  fechaRegistro: dayjs('2023-03-13'),
  fechaPago: dayjs('2023-03-13'),
  tipoPago: TiposPagos['DIARIO'],
  descripcion: 'Configuración añadido Market',
};

export const sampleWithPartialData: IPagos = {
  id: 90317,
  montoPago: 39482,
  montoInicial: 75237,
  saldo: 22940,
  fechaRegistro: dayjs('2023-03-13'),
  fechaPago: dayjs('2023-03-13'),
  tipoPago: TiposPagos['CUOTA'],
  descripcion: 'Lugar',
};

export const sampleWithFullData: IPagos = {
  id: 29917,
  montoPago: 3506,
  montoInicial: 94274,
  saldo: 57695,
  fechaRegistro: dayjs('2023-03-13'),
  fechaPago: dayjs('2023-03-13'),
  tipoPago: TiposPagos['DIARIO'],
  descripcion: 'COM',
};

export const sampleWithNewData: NewPagos = {
  montoPago: 6073,
  montoInicial: 17805,
  saldo: 50968,
  fechaRegistro: dayjs('2023-03-13'),
  fechaPago: dayjs('2023-03-13'),
  tipoPago: TiposPagos['MENSUAL'],
  descripcion: 'Violeta',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
