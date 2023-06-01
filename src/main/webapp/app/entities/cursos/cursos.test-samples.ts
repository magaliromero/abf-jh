import dayjs from 'dayjs/esm';

import { Niveles } from 'app/entities/enumerations/niveles.model';

import { ICursos, NewCursos } from './cursos.model';

export const sampleWithRequiredData: ICursos = {
  id: 88953,
  nombreCurso: 'Washington channels Djibouti',
  descripcion: 'back-end',
  nivel: Niveles['PREAJEDREZ'],
};

export const sampleWithPartialData: ICursos = {
  id: 96032,
  nombreCurso: 'dot-com Operations',
  descripcion: 'Mississippi',
  fechaInicio: dayjs('2023-06-01'),
  nivel: Niveles['INICIAL'],
};

export const sampleWithFullData: ICursos = {
  id: 78666,
  nombreCurso: 'microchip',
  descripcion: 'solid Fish',
  fechaInicio: dayjs('2023-06-01'),
  fechaFin: dayjs('2023-06-01'),
  cantidadClases: 82546,
  nivel: Niveles['INICIAL'],
};

export const sampleWithNewData: NewCursos = {
  nombreCurso: 'silver',
  descripcion: 'Generic digital indigo',
  nivel: Niveles['INICIAL'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
