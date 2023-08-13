import dayjs from 'dayjs/esm';

import { EstadosPagos } from 'app/entities/enumerations/estados-pagos.model';

import { IMatricula, NewMatricula } from './matricula.model';

export const sampleWithRequiredData: IMatricula = {
  id: 55032,
  concepto: 'Ouguiya Keyboard AI',
  monto: 45850,
  fechaInscripcion: dayjs('2023-05-23'),
  fechaInicio: dayjs('2023-05-23'),
  estado: EstadosPagos['PENDIENTE'],
};

export const sampleWithPartialData: IMatricula = {
  id: 73560,
  concepto: 'Sausages generating Village',
  monto: 95292,
  fechaInscripcion: dayjs('2023-05-23'),
  fechaInicio: dayjs('2023-05-23'),
  fechaPago: dayjs('2023-05-22'),
  estado: EstadosPagos['ANULADO'],
};

export const sampleWithFullData: IMatricula = {
  id: 99950,
  concepto: 'CFP world-class',
  monto: 65690,
  fechaInscripcion: dayjs('2023-05-23'),
  fechaInicio: dayjs('2023-05-23'),
  fechaPago: dayjs('2023-05-23'),
  estado: EstadosPagos['ANULADO'],
};

export const sampleWithNewData: NewMatricula = {
  concepto: 'Music',
  monto: 85587,
  fechaInscripcion: dayjs('2023-05-23'),
  fechaInicio: dayjs('2023-05-23'),
  estado: EstadosPagos['PENDIENTE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
