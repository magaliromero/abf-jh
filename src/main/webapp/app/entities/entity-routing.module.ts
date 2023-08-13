import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'temas',
        data: { pageTitle: 'abfApp.temas.home.title' },
        loadChildren: () => import('./temas/temas.module').then(m => m.TemasModule),
      },
      {
        path: 'registro-clases',
        data: { pageTitle: 'abfApp.registroClases.home.title' },
        loadChildren: () => import('./registro-clases/registro-clases.module').then(m => m.RegistroClasesModule),
      },
      {
        path: 'tipos-documentos',
        data: { pageTitle: 'abfApp.tiposDocumentos.home.title' },
        loadChildren: () => import('./tipos-documentos/tipos-documentos.module').then(m => m.TiposDocumentosModule),
      },
      {
        path: 'alumnos',
        data: { pageTitle: 'abfApp.alumnos.home.title' },
        loadChildren: () => import('./alumnos/alumnos.module').then(m => m.AlumnosModule),
      },
      {
        path: 'funcionarios',
        data: { pageTitle: 'abfApp.funcionarios.home.title' },
        loadChildren: () => import('./funcionarios/funcionarios.module').then(m => m.FuncionariosModule),
      },
      {
        path: 'facturas',
        data: { pageTitle: 'abfApp.facturas.home.title' },
        loadChildren: () => import('./facturas/facturas.module').then(m => m.FacturasModule),
      },
      {
        path: 'factura-detalle',
        data: { pageTitle: 'abfApp.facturaDetalle.home.title' },
        loadChildren: () => import('./factura-detalle/factura-detalle.module').then(m => m.FacturaDetalleModule),
      },
      {
        path: 'pagos',
        data: { pageTitle: 'abfApp.pagos.home.title' },
        loadChildren: () => import('./pagos/pagos.module').then(m => m.PagosModule),
      },
      {
        path: 'productos',
        data: { pageTitle: 'abfApp.productos.home.title' },
        loadChildren: () => import('./productos/productos.module').then(m => m.ProductosModule),
      },
      {
        path: 'materiales',
        data: { pageTitle: 'abfApp.materiales.home.title' },
        loadChildren: () => import('./materiales/materiales.module').then(m => m.MaterialesModule),
      },
      {
        path: 'prestamos',
        data: { pageTitle: 'abfApp.prestamos.home.title' },
        loadChildren: () => import('./prestamos/prestamos.module').then(m => m.PrestamosModule),
      },
      {
        path: 'matricula',
        data: { pageTitle: 'abfApp.matricula.home.title' },
        loadChildren: () => import('./matricula/matricula.module').then(m => m.MatriculaModule),
      },
      {
        path: 'evaluaciones',
        data: { pageTitle: 'abfApp.evaluaciones.home.title' },
        loadChildren: () => import('./evaluaciones/evaluaciones.module').then(m => m.EvaluacionesModule),
      },
      {
        path: 'evaluaciones-detalle',
        data: { pageTitle: 'abfApp.evaluacionesDetalle.home.title' },
        loadChildren: () => import('./evaluaciones-detalle/evaluaciones-detalle.module').then(m => m.EvaluacionesDetalleModule),
      },
      {
        path: 'cursos',
        data: { pageTitle: 'abfApp.cursos.home.title' },
        loadChildren: () => import('./cursos/cursos.module').then(m => m.CursosModule),
      },
      {
        path: 'inscripciones',
        data: { pageTitle: 'abfApp.inscripciones.home.title' },
        loadChildren: () => import('./inscripciones/inscripciones.module').then(m => m.InscripcionesModule),
      },
      {
        path: 'clientes',
        data: { pageTitle: 'abfApp.clientes.home.title' },
        loadChildren: () => import('./clientes/clientes.module').then(m => m.ClientesModule),
      },
      {
        path: 'timbrados',
        data: { pageTitle: 'abfApp.timbrados.home.title' },
        loadChildren: () => import('./timbrados/timbrados.module').then(m => m.TimbradosModule),
      },
      {
        path: 'sucursales',
        data: { pageTitle: 'abfApp.sucursales.home.title' },
        loadChildren: () => import('./sucursales/sucursales.module').then(m => m.SucursalesModule),
      },
      {
        path: 'punto-de-expedicion',
        data: { pageTitle: 'abfApp.puntoDeExpedicion.home.title' },
        loadChildren: () => import('./punto-de-expedicion/punto-de-expedicion.module').then(m => m.PuntoDeExpedicionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
