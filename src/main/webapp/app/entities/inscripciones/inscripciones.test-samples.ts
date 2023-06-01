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
  fechaInscripcion: dayjs('2023-06-01'),
};

export const sampleWithNewData: NewInscripciones = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
