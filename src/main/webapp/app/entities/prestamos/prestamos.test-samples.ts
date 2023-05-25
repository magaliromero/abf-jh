import dayjs from 'dayjs/esm';

import { EstadosPrestamos } from 'app/entities/enumerations/estados-prestamos.model';

import { IPrestamos, NewPrestamos } from './prestamos.model';

export const sampleWithRequiredData: IPrestamos = {
  id: 9135,
  fechaPrestamo: dayjs('2023-05-23'),
  estado: EstadosPrestamos['DEVUELTO'],
};

export const sampleWithPartialData: IPrestamos = {
  id: 94363,
  fechaPrestamo: dayjs('2023-05-23'),
  estado: EstadosPrestamos['VENCIDO'],
};

export const sampleWithFullData: IPrestamos = {
  id: 94689,
  fechaPrestamo: dayjs('2023-05-23'),
  fechaDevolucion: dayjs('2023-05-23'),
  estado: EstadosPrestamos['VENCIDO'],
};

export const sampleWithNewData: NewPrestamos = {
  fechaPrestamo: dayjs('2023-05-23'),
  estado: EstadosPrestamos['PRESTADO'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
