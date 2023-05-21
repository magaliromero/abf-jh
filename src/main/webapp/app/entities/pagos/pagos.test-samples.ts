import dayjs from 'dayjs/esm';

import { IPagos, NewPagos } from './pagos.model';

export const sampleWithRequiredData: IPagos = {
  id: 35750,
  fecha: dayjs('2023-05-20'),
  total: 36645,
  cantidadHoras: 2075,
};

export const sampleWithPartialData: IPagos = {
  id: 12142,
  fecha: dayjs('2023-05-20'),
  total: 60113,
  cantidadHoras: 85247,
};

export const sampleWithFullData: IPagos = {
  id: 92852,
  fecha: dayjs('2023-05-20'),
  total: 97277,
  cantidadHoras: 29305,
};

export const sampleWithNewData: NewPagos = {
  fecha: dayjs('2023-05-20'),
  total: 94228,
  cantidadHoras: 60224,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
