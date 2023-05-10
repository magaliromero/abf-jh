import { EstadosMateriales } from 'app/entities/enumerations/estados-materiales.model';

import { IMateriales, NewMateriales } from './materiales.model';

export const sampleWithRequiredData: IMateriales = {
  id: 51682,
  descripcion: 'Hecho Avanzado withdrawal',
  estado: EstadosMateriales['VENCIDO'],
  cantidadStock: 10372,
  cantidadPrestamo: 90659,
};

export const sampleWithPartialData: IMateriales = {
  id: 87539,
  descripcion: 'Account Algodón País',
  estado: EstadosMateriales['PRESTADO'],
  cantidadStock: 87558,
  cantidadPrestamo: 19129,
};

export const sampleWithFullData: IMateriales = {
  id: 1247,
  descripcion: 'transmitter',
  estado: EstadosMateriales['DISPONIBLE'],
  cantidadStock: 33751,
  cantidadPrestamo: 37480,
};

export const sampleWithNewData: NewMateriales = {
  descripcion: 'Syrian Rústico vortals',
  estado: EstadosMateriales['DISPONIBLE'],
  cantidadStock: 19341,
  cantidadPrestamo: 59205,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
