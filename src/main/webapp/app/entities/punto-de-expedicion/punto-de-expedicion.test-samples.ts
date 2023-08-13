import { IPuntoDeExpedicion, NewPuntoDeExpedicion } from './punto-de-expedicion.model';

export const sampleWithRequiredData: IPuntoDeExpedicion = {
  id: 49237,
  numeroPuntoDeExpedicion: 'EXE cross-media',
};

export const sampleWithPartialData: IPuntoDeExpedicion = {
  id: 48781,
  numeroPuntoDeExpedicion: 'Concrete Gold',
};

export const sampleWithFullData: IPuntoDeExpedicion = {
  id: 81307,
  numeroPuntoDeExpedicion: 'granular connecting Frozen',
};

export const sampleWithNewData: NewPuntoDeExpedicion = {
  numeroPuntoDeExpedicion: 'back backing',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
