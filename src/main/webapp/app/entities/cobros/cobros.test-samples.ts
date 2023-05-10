import dayjs from 'dayjs/esm';

import { TiposPagos } from 'app/entities/enumerations/tipos-pagos.model';

import { ICobros, NewCobros } from './cobros.model';

export const sampleWithRequiredData: ICobros = {
  id: 34509,
  montoPago: 65588,
  montoInicial: 17906,
  saldo: 51174,
  fechaRegistro: dayjs('2023-04-05'),
  fechaPago: dayjs('2023-04-05'),
  tipoPago: TiposPagos['MENSUAL'],
  descripcion: 'invoice innovative',
};

export const sampleWithPartialData: ICobros = {
  id: 22781,
  montoPago: 69119,
  montoInicial: 10737,
  saldo: 59201,
  fechaRegistro: dayjs('2023-04-05'),
  fechaPago: dayjs('2023-04-05'),
  tipoPago: TiposPagos['PARCIAL'],
  descripcion: 'Mascotas Parafarmacia calculating',
};

export const sampleWithFullData: ICobros = {
  id: 88228,
  montoPago: 95306,
  montoInicial: 69518,
  saldo: 96854,
  fechaRegistro: dayjs('2023-04-05'),
  fechaPago: dayjs('2023-04-05'),
  tipoPago: TiposPagos['DIARIO'],
  descripcion: 'Algodón',
};

export const sampleWithNewData: NewCobros = {
  montoPago: 61153,
  montoInicial: 45390,
  saldo: 4725,
  fechaRegistro: dayjs('2023-04-05'),
  fechaPago: dayjs('2023-04-05'),
  tipoPago: TiposPagos['DIARIO'],
  descripcion: 'cross-platform convergence withdrawal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
