import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
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
        path: 'temas',
        data: { pageTitle: 'abfApp.temas.home.title' },
        loadChildren: () => import('./temas/temas.module').then(m => m.TemasModule),
      },
      {
        path: 'malla-curricular',
        data: { pageTitle: 'abfApp.mallaCurricular.home.title' },
        loadChildren: () => import('./malla-curricular/malla-curricular.module').then(m => m.MallaCurricularModule),
      },
      {
        path: 'matricula',
        data: { pageTitle: 'abfApp.matricula.home.title' },
        loadChildren: () => import('./matricula/matricula.module').then(m => m.MatriculaModule),
      },
      {
        path: 'registro-clases',
        data: { pageTitle: 'abfApp.registroClases.home.title' },
        loadChildren: () => import('./registro-clases/registro-clases.module').then(m => m.RegistroClasesModule),
      },
      {
        path: 'pagos',
        data: { pageTitle: 'abfApp.pagos.home.title' },
        loadChildren: () => import('./pagos/pagos.module').then(m => m.PagosModule),
      },
      {
        path: 'usuarios',
        data: { pageTitle: 'abfApp.usuarios.home.title' },
        loadChildren: () => import('./usuarios/usuarios.module').then(m => m.UsuariosModule),
      },
      {
        path: 'prestamos',
        data: { pageTitle: 'abfApp.prestamos.home.title' },
        loadChildren: () => import('./prestamos/prestamos.module').then(m => m.PrestamosModule),
      },
      {
        path: 'torneos',
        data: { pageTitle: 'abfApp.torneos.home.title' },
        loadChildren: () => import('./torneos/torneos.module').then(m => m.TorneosModule),
      },
      {
        path: 'materiales',
        data: { pageTitle: 'abfApp.materiales.home.title' },
        loadChildren: () => import('./materiales/materiales.module').then(m => m.MaterialesModule),
      },
      {
        path: 'ficha-partidas-torneos',
        data: { pageTitle: 'abfApp.fichaPartidasTorneos.home.title' },
        loadChildren: () => import('./ficha-partidas-torneos/ficha-partidas-torneos.module').then(m => m.FichaPartidasTorneosModule),
      },
      {
        path: 'cursos',
        data: { pageTitle: 'abfApp.cursos.home.title' },
        loadChildren: () => import('./cursos/cursos.module').then(m => m.CursosModule),
      },
      {
        path: 'evaluaciones',
        data: { pageTitle: 'abfApp.evaluaciones.home.title' },
        loadChildren: () => import('./evaluaciones/evaluaciones.module').then(m => m.EvaluacionesModule),
      },
      {
        path: 'inscripciones',
        data: { pageTitle: 'abfApp.inscripciones.home.title' },
        loadChildren: () => import('./inscripciones/inscripciones.module').then(m => m.InscripcionesModule),
      },
      {
        path: 'cobros',
        data: { pageTitle: 'abfApp.cobros.home.title' },
        loadChildren: () => import('./cobros/cobros.module').then(m => m.CobrosModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
