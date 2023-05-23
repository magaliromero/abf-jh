import { IEvaluacionesDetalle, NewEvaluacionesDetalle } from './evaluaciones-detalle.model';

export const sampleWithRequiredData: IEvaluacionesDetalle = {
  id: 78677,
  comentarios: 'IB',
  puntaje: 43633,
};

export const sampleWithPartialData: IEvaluacionesDetalle = {
  id: 95549,
  comentarios: 'parsing Thailand',
  puntaje: 91838,
};

export const sampleWithFullData: IEvaluacionesDetalle = {
  id: 14654,
  comentarios: 'policy Verde Data',
  puntaje: 71748,
};

export const sampleWithNewData: NewEvaluacionesDetalle = {
  comentarios: 'mission-critical',
  puntaje: 99187,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
