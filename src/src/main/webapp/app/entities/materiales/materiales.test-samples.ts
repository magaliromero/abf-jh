import { IMateriales, NewMateriales } from './materiales.model';

export const sampleWithRequiredData: IMateriales = {
  id: 51682,
  descripcion: 'Handcrafted Ameliorated withdrawal',
};

export const sampleWithPartialData: IMateriales = {
  id: 90659,
  descripcion: 'Orchestrator hack Toys',
  cantidad: 20460,
};

export const sampleWithFullData: IMateriales = {
  id: 59429,
  descripcion: 'red transmitter Fish',
  cantidad: 76470,
  cantidadEnPrestamo: 43346,
};

export const sampleWithNewData: NewMateriales = {
  descripcion: 'Chicken',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
