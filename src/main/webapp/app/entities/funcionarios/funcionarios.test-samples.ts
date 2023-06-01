import dayjs from 'dayjs/esm';

import { EstadosPersona } from 'app/entities/enumerations/estados-persona.model';
import { TipoFuncionarios } from 'app/entities/enumerations/tipo-funcionarios.model';

import { IFuncionarios, NewFuncionarios } from './funcionarios.model';

export const sampleWithRequiredData: IFuncionarios = {
  id: 25530,
  nombres: 'Ergonomic',
  apellidos: 'programming',
  nombreCompleto: 'Sports copying',
  email: 'Remington51@yahoo.com',
  telefono: 'deposit seamless',
  fechaNacimiento: dayjs('2023-06-01'),
  documento: 'Credit tan',
  estado: EstadosPersona['ACTIVO'],
};

export const sampleWithPartialData: IFuncionarios = {
  id: 66416,
  nombres: 'Pre-emptive',
  apellidos: 'utilize',
  nombreCompleto: 'Self-enabling',
  email: 'Florian.Hickle94@hotmail.com',
  telefono: 'orchestrate Massachusetts Decentralized',
  fechaNacimiento: dayjs('2023-06-01'),
  documento: 'synergy Avon',
  estado: EstadosPersona['ACTIVO'],
};

export const sampleWithFullData: IFuncionarios = {
  id: 2032,
  elo: 28354,
  fideId: 25451,
  nombres: 'bypassing capacitor benchmark',
  apellidos: 'payment',
  nombreCompleto: 'Agent Devolved Cambridgeshire',
  email: 'Jedidiah_Treutel@hotmail.com',
  telefono: 'Wooden',
  fechaNacimiento: dayjs('2023-06-01'),
  documento: 'communities',
  estado: EstadosPersona['INACTIVO'],
  tipoFuncionario: TipoFuncionarios['OTRO'],
};

export const sampleWithNewData: NewFuncionarios = {
  nombres: 'user real-time',
  apellidos: 'Agent Maine Pound',
  nombreCompleto: 'Towels',
  email: 'Branson_Padberg@yahoo.com',
  telefono: 'Cambridgeshire online',
  fechaNacimiento: dayjs('2023-06-01'),
  documento: 'quantifying Pants',
  estado: EstadosPersona['ACTIVO'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
