import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductosComponent } from './list/productos.component';
import { ProductosDetailComponent } from './detail/productos-detail.component';
import { ProductosUpdateComponent } from './update/productos-update.component';
import { ProductosDeleteDialogComponent } from './delete/productos-delete-dialog.component';
import { ProductosRoutingModule } from './route/productos-routing.module';

@NgModule({
  imports: [SharedModule, ProductosRoutingModule],
  declarations: [ProductosComponent, ProductosDetailComponent, ProductosUpdateComponent, ProductosDeleteDialogComponent],
})
export class ProductosModule {}
