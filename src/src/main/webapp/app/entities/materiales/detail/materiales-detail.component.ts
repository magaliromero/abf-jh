import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMateriales } from '../materiales.model';

@Component({
  selector: 'jhi-materiales-detail',
  templateUrl: './materiales-detail.component.html',
})
export class MaterialesDetailComponent implements OnInit {
  materiales: IMateriales | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ materiales }) => {
      this.materiales = materiales;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
