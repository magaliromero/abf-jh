import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITiposDocumentos } from '../tipos-documentos.model';

@Component({
  selector: 'jhi-tipos-documentos-detail',
  templateUrl: './tipos-documentos-detail.component.html',
})
export class TiposDocumentosDetailComponent implements OnInit {
  tiposDocumentos: ITiposDocumentos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tiposDocumentos }) => {
      this.tiposDocumentos = tiposDocumentos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
