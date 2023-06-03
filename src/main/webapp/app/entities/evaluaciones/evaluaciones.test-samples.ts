import dayjs from 'dayjs/esm';

import { IEvaluaciones, NewEvaluaciones } from './evaluaciones.model';

export const sampleWithRequiredData: IEvaluaciones = {
  id: 68068,
  nroEvaluacion: 79635,
  fecha: dayjs('2023-05-23'),
};

export const sampleWithPartialData: IEvaluaciones = {
  id: 13734,
  nroEvaluacion: 76243,
  fecha: dayjs('2023-05-23'),
};

export const sampleWithFullData: IEvaluaciones = {
  id: 17700,
  nroEvaluacion: 69520,
  fecha: dayjs('2023-05-23'),
};

export const sampleWithNewData: NewEvaluaciones = {
  nroEvaluacion: 52691,
  fecha: dayjs('2023-05-23'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
