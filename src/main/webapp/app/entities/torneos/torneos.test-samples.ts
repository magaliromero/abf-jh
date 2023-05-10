import dayjs from 'dayjs/esm';

import { TiposTorneos } from 'app/entities/enumerations/tipos-torneos.model';

import { ITorneos, NewTorneos } from './torneos.model';

export const sampleWithRequiredData: ITorneos = {
  id: 72448,
  nombreTorneo: 'state',
  fechaInicio: dayjs('2023-03-12'),
  fechaFin: dayjs('2023-03-13'),
  lugar: 'Zapatos index array',
  tipoTorneo: TiposTorneos['NACIONAL'],
  torneoEvaluado: true,
  federado: false,
};

export const sampleWithPartialData: ITorneos = {
  id: 38900,
  nombreTorneo: 'invoice',
  fechaInicio: dayjs('2023-03-13'),
  fechaFin: dayjs('2023-03-13'),
  lugar: 'calculating',
  tipoTorneo: TiposTorneos['INTERNO'],
  torneoEvaluado: true,
  federado: true,
};

export const sampleWithFullData: ITorneos = {
  id: 99261,
  nombreTorneo: '1080p',
  fechaInicio: dayjs('2023-03-13'),
  fechaFin: dayjs('2023-03-13'),
  lugar: 'holística Montserrat',
  tiempo: 'next-generation',
  tipoTorneo: TiposTorneos['INTERNO'],
  torneoEvaluado: false,
  federado: true,
};

export const sampleWithNewData: NewTorneos = {
  nombreTorneo: 'optimize',
  fechaInicio: dayjs('2023-03-13'),
  fechaFin: dayjs('2023-03-13'),
  lugar: 'synthesize Apartamento back',
  tipoTorneo: TiposTorneos['INTERNACIONAL'],
  torneoEvaluado: true,
  federado: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
