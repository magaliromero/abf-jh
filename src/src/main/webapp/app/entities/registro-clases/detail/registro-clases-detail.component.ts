import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegistroClases } from '../registro-clases.model';

@Component({
  selector: 'jhi-registro-clases-detail',
  templateUrl: './registro-clases-detail.component.html',
})
export class RegistroClasesDetailComponent implements OnInit {
  registroClases: IRegistroClases | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registroClases }) => {
      this.registroClases = registroClases;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
