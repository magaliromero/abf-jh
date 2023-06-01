import dayjs from 'dayjs/esm';

import { IRegistroClases, NewRegistroClases } from './registro-clases.model';

export const sampleWithRequiredData: IRegistroClases = {
  id: 57702,
  fecha: dayjs('2023-06-01'),
  cantidadHoras: 61826,
};

export const sampleWithPartialData: IRegistroClases = {
  id: 32560,
  fecha: dayjs('2023-05-31'),
  cantidadHoras: 70147,
};

export const sampleWithFullData: IRegistroClases = {
  id: 58298,
  fecha: dayjs('2023-06-01'),
  cantidadHoras: 59361,
  asistenciaAlumno: true,
};

export const sampleWithNewData: NewRegistroClases = {
  fecha: dayjs('2023-06-01'),
  cantidadHoras: 26022,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
