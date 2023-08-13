export interface ISucursales {
  id: number;
  nombreSucursal?: string | null;
  direccion?: string | null;
  numeroEstablecimiento?: string | null;
}

export type NewSucursales = Omit<ISucursales, 'id'> & { id: null };
