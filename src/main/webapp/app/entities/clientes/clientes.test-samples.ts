import dayjs from 'dayjs/esm';

import { IClientes, NewClientes } from './clientes.model';

export const sampleWithRequiredData: IClientes = {
  id: 88470,
  nombres: 'Auto Nevada partnerships',
  apellidos: 'Practical',
  razonSocial: 'Keyboard',
  documento: 'Turnpike intangible',
};

export const sampleWithPartialData: IClientes = {
  id: 38520,
  nombres: 'synergize explicit disintermediate',
  apellidos: 'functionalities Sports',
  razonSocial: 'Electronics',
  documento: 'productize',
  email: 'Yasmine.Nikolaus71@yahoo.com',
  direccion: 'online',
};

export const sampleWithFullData: IClientes = {
  id: 62321,
  ruc: 'payment',
  nombres: 'Comoro Frozen Unbranded',
  apellidos: 'leverage Plastic',
  razonSocial: 'paradigm',
  documento: 'matrix Personal Michigan',
  email: 'Presley_Lueilwitz@yahoo.com',
  telefono: 'Ethiopia Chief',
  fechaNacimiento: dayjs('2023-06-01'),
  direccion: 'Customer Open-architected',
};

export const sampleWithNewData: NewClientes = {
  nombres: 'Tuna',
  apellidos: 'Administrator support Cross-group',
  razonSocial: 'Division Account Turkey',
  documento: 'eyeballs',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
