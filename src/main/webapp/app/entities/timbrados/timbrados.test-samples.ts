import dayjs from 'dayjs/esm';

import { ITimbrados, NewTimbrados } from './timbrados.model';

export const sampleWithRequiredData: ITimbrados = {
  id: 60634,
  numeroTimbrado: 'program responsive',
  fechaInicio: dayjs('2023-08-13'),
  fechaFin: dayjs('2023-08-13'),
};

export const sampleWithPartialData: ITimbrados = {
  id: 56853,
  numeroTimbrado: 'Operative Georgia',
  fechaInicio: dayjs('2023-08-13'),
  fechaFin: dayjs('2023-08-13'),
};

export const sampleWithFullData: ITimbrados = {
  id: 25214,
  numeroTimbrado: 'optical',
  fechaInicio: dayjs('2023-08-12'),
  fechaFin: dayjs('2023-08-13'),
};

export const sampleWithNewData: NewTimbrados = {
  numeroTimbrado: 'Games',
  fechaInicio: dayjs('2023-08-13'),
  fechaFin: dayjs('2023-08-12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
