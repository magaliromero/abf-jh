import dayjs from 'dayjs/esm';

import { IInscripciones, NewInscripciones } from './inscripciones.model';

export const sampleWithRequiredData: IInscripciones = {
  id: 63115,
};

export const sampleWithPartialData: IInscripciones = {
  id: 64205,
};

export const sampleWithFullData: IInscripciones = {
  id: 111,
  fecha: dayjs('2023-03-13'),
};

export const sampleWithNewData: NewInscripciones = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
