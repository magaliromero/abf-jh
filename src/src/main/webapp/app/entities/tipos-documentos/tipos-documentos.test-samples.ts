import { ITiposDocumentos, NewTiposDocumentos } from './tipos-documentos.model';

export const sampleWithRequiredData: ITiposDocumentos = {
  id: 68091,
  codigo: 'connect deposit Small',
  descripcion: 'Peso Avon Loan',
};

export const sampleWithPartialData: ITiposDocumentos = {
  id: 41468,
  codigo: 'Associate leverage applications',
  descripcion: 'Ville dot-com',
};

export const sampleWithFullData: ITiposDocumentos = {
  id: 36840,
  codigo: 'Analyst Malaysia',
  descripcion: 'CSS SCSI',
};

export const sampleWithNewData: NewTiposDocumentos = {
  codigo: 'Consultant Outdoors Soft',
  descripcion: 'RSS index',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
