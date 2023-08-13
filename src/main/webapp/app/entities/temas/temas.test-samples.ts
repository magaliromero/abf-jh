import { ITemas, NewTemas } from './temas.model';

export const sampleWithRequiredData: ITemas = {
  id: 89167,
  titulo: 'connecting Concrete',
  descripcion: 'functionalities SSL virtual',
};

export const sampleWithPartialData: ITemas = {
  id: 18783,
  titulo: 'GB',
  descripcion: 'enterprise',
};

export const sampleWithFullData: ITemas = {
  id: 56707,
  titulo: 'mission-critical',
  descripcion: 'Legacy Pataca Account',
};

export const sampleWithNewData: NewTemas = {
  titulo: 'Coordinator enable',
  descripcion: 'architectures Tools',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
