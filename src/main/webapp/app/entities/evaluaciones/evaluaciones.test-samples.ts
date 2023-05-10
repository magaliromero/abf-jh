import dayjs from 'dayjs/esm';

import { TiposEvaluaciones } from 'app/entities/enumerations/tipos-evaluaciones.model';

import { IEvaluaciones, NewEvaluaciones } from './evaluaciones.model';

export const sampleWithRequiredData: IEvaluaciones = {
  id: 68068,
  tipoEvaluacion: TiposEvaluaciones['FORMATIVA'],
  fecha: dayjs('2023-03-13'),
};

export const sampleWithPartialData: IEvaluaciones = {
  id: 91593,
  tipoEvaluacion: TiposEvaluaciones['ACUMULATIVA'],
  idActa: 69398,
  fecha: dayjs('2023-03-13'),
  puntosLogrados: 55864,
  comentarios: 'Distrito',
};

export const sampleWithFullData: IEvaluaciones = {
  id: 66628,
  tipoEvaluacion: TiposEvaluaciones['FORMATIVA'],
  idExamen: 82772,
  idActa: 50767,
  fecha: dayjs('2023-03-13'),
  puntosLogrados: 30805,
  porcentaje: 1145,
  comentarios: 'channels',
};

export const sampleWithNewData: NewEvaluaciones = {
  tipoEvaluacion: TiposEvaluaciones['FINAL'],
  fecha: dayjs('2023-03-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
