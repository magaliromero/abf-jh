import { ISucursales, NewSucursales } from './sucursales.model';

export const sampleWithRequiredData: ISucursales = {
  id: 64468,
  nombreSucursal: 'Land 1080p relationships',
  numeroEstablecimiento: 'THX Ball',
};

export const sampleWithPartialData: ISucursales = {
  id: 79736,
  nombreSucursal: 'interface black Pula',
  direccion: 'full-range JSON',
  numeroEstablecimiento: 'Port',
};

export const sampleWithFullData: ISucursales = {
  id: 91578,
  nombreSucursal: 'Unions',
  direccion: '1080p copying',
  numeroEstablecimiento: 'Unions Frozen',
};

export const sampleWithNewData: NewSucursales = {
  nombreSucursal: 'bandwidth-monitored',
  numeroEstablecimiento: 'Netherlands',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
