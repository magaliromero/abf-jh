import { Niveles } from 'app/entities/enumerations/niveles.model';

import { ITemas, NewTemas } from './temas.model';

export const sampleWithRequiredData: ITemas = {
  id: 89167,
  titulo: 'connecting Concrete',
  descripcion: 'functionalities SSL virtual',
  nivel: Niveles['INICIAL'],
};

export const sampleWithPartialData: ITemas = {
  id: 12021,
  titulo: 'Gloves enterprise Iowa',
  descripcion: 'Spurs',
  nivel: Niveles['AVANZADO'],
};

export const sampleWithFullData: ITemas = {
  id: 96305,
  titulo: 'Turkmenistan Jewelery Coordinator',
  descripcion: 'Steel architectures',
  nivel: Niveles['PREAJEDREZ'],
};

export const sampleWithNewData: NewTemas = {
  titulo: 'Soft cohesive',
  descripcion: 'Vermont Missouri',
  nivel: Niveles['TODOS'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
