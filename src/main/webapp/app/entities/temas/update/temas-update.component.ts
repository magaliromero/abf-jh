import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TemasFormService, TemasFormGroup } from './temas-form.service';
import { ITemas } from '../temas.model';
import { TemasService } from '../service/temas.service';

@Component({
  selector: 'jhi-temas-update',
  templateUrl: './temas-update.component.html',
})
export class TemasUpdateComponent implements OnInit {
  isSaving = false;
  temas: ITemas | null = null;

  editForm: TemasFormGroup = this.temasFormService.createTemasFormGroup();

  constructor(
    protected temasService: TemasService,
    protected temasFormService: TemasFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temas }) => {
      this.temas = temas;
      if (temas) {
        this.updateForm(temas);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const temas = this.temasFormService.getTemas(this.editForm);
    if (temas.id !== null) {
      this.subscribeToSaveResponse(this.temasService.update(temas));
    } else {
      this.subscribeToSaveResponse(this.temasService.create(temas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITemas>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(temas: ITemas): void {
    this.temas = temas;
    this.temasFormService.resetForm(this.editForm, temas);
  }
}
