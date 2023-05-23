import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FuncionariosComponent } from './list/funcionarios.component';
import { FuncionariosDetailComponent } from './detail/funcionarios-detail.component';
import { FuncionariosUpdateComponent } from './update/funcionarios-update.component';
import { FuncionariosDeleteDialogComponent } from './delete/funcionarios-delete-dialog.component';
import { FuncionariosRoutingModule } from './route/funcionarios-routing.module';

@NgModule({
  imports: [SharedModule, FuncionariosRoutingModule],
  declarations: [FuncionariosComponent, FuncionariosDetailComponent, FuncionariosUpdateComponent, FuncionariosDeleteDialogComponent],
})
export class FuncionariosModule {}
