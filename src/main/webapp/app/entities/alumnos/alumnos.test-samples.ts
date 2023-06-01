import dayjs from 'dayjs/esm';

import { EstadosPersona } from 'app/entities/enumerations/estados-persona.model';

import { IAlumnos, NewAlumnos } from './alumnos.model';

export const sampleWithRequiredData: IAlumnos = {
  id: 67399,
  nombres: 'Licensed',
  apellidos: 'Cambridgeshire',
  nombreCompleto: 'Future-proofed Direct International',
  telefono: 'Ohio Electronics mobile',
  fechaNacimiento: dayjs('2023-06-01'),
  documento: 'help-desk magnetic migration',
  estado: EstadosPersona['ACTIVO'],
};

export const sampleWithPartialData: IAlumnos = {
  id: 33002,
  elo: 30108,
  nombres: 'eyeballs',
  apellidos: 'Shoes National',
  nombreCompleto: 'AI transmitting',
  telefono: 'orange Washington',
  fechaNacimiento: dayjs('2023-06-01'),
  documento: 'architecture Kenyan Mongolia',
  estado: EstadosPersona['ACTIVO'],
};

export const sampleWithFullData: IAlumnos = {
  id: 74829,
  elo: 70893,
  fideId: 4894,
  nombres: 'Mouse Bike e-business',
  apellidos: 'Hill Small',
  nombreCompleto: 'channels HTTP Human',
  email: 'Reta30@gmail.com',
  telefono: 'payment ivory',
  fechaNacimiento: dayjs('2023-06-01'),
  documento: 'Wooden Architect e-commerce',
  estado: EstadosPersona['ACTIVO'],
};

export const sampleWithNewData: NewAlumnos = {
  nombres: 'Granite auxiliary',
  apellidos: 'generate Bedfordshire cyan',
  nombreCompleto: 'Account',
  telefono: 'cross-platform',
  fechaNacimiento: dayjs('2023-06-01'),
  documento: 'Auto Market',
  estado: EstadosPersona['ACTIVO'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
