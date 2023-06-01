import dayjs from 'dayjs/esm';

export interface IClientes {
  id: number;
  ruc?: string | null;
  nombres?: string | null;
  apellidos?: string | null;
  razonSocial?: string | null;
  documento?: string | null;
  email?: string | null;
  telefono?: string | null;
  fechaNacimiento?: dayjs.Dayjs | null;
  direccion?: string | null;
}

export type NewClientes = Omit<IClientes, 'id'> & { id: null };
