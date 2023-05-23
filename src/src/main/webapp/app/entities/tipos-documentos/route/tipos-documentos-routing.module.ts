import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TiposDocumentosComponent } from '../list/tipos-documentos.component';
import { TiposDocumentosDetailComponent } from '../detail/tipos-documentos-detail.component';
import { TiposDocumentosUpdateComponent } from '../update/tipos-documentos-update.component';
import { TiposDocumentosRoutingResolveService } from './tipos-documentos-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tiposDocumentosRoute: Routes = [
  {
    path: '',
    component: TiposDocumentosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TiposDocumentosDetailComponent,
    resolve: {
      tiposDocumentos: TiposDocumentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TiposDocumentosUpdateComponent,
    resolve: {
      tiposDocumentos: TiposDocumentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TiposDocumentosUpdateComponent,
    resolve: {
      tiposDocumentos: TiposDocumentosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tiposDocumentosRoute)],
  exports: [RouterModule],
})
export class TiposDocumentosRoutingModule {}
