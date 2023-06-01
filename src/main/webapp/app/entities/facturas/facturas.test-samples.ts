import dayjs from 'dayjs/esm';

import { CondicionVenta } from 'app/entities/enumerations/condicion-venta.model';

import { IFacturas, NewFacturas } from './facturas.model';

export const sampleWithRequiredData: IFacturas = {
  id: 29679,
  fecha: dayjs('2023-06-01'),
  facturaNro: 'explicit circuit',
  timbrado: 61190,
  razonSocial: 'Zealand',
  ruc: 'SMS Rubber Market',
  condicionVenta: CondicionVenta['CONTADO'],
  total: 86011,
};

export const sampleWithPartialData: IFacturas = {
  id: 37081,
  fecha: dayjs('2023-06-01'),
  facturaNro: 'Oval',
  timbrado: 89640,
  razonSocial: 'Yuan Tasty',
  ruc: 'USB mesh Virginia',
  condicionVenta: CondicionVenta['CONTADO'],
  total: 3999,
};

export const sampleWithFullData: IFacturas = {
  id: 97576,
  fecha: dayjs('2023-06-01'),
  facturaNro: 'National',
  timbrado: 22694,
  razonSocial: 'bypassing Configuration navigate',
  ruc: 'Bacon tan',
  condicionVenta: CondicionVenta['CONTADO'],
  total: 5477,
};

export const sampleWithNewData: NewFacturas = {
  fecha: dayjs('2023-06-01'),
  facturaNro: 'cohesive Greece',
  timbrado: 59500,
  razonSocial: 'XML protocol',
  ruc: 'feed Investor Digitized',
  condicionVenta: CondicionVenta['CONTADO'],
  total: 12789,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
