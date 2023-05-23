import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TiposDocumentosFormService } from './tipos-documentos-form.service';
import { TiposDocumentosService } from '../service/tipos-documentos.service';
import { ITiposDocumentos } from '../tipos-documentos.model';

import { TiposDocumentosUpdateComponent } from './tipos-documentos-update.component';

describe('TiposDocumentos Management Update Component', () => {
  let comp: TiposDocumentosUpdateComponent;
  let fixture: ComponentFixture<TiposDocumentosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tiposDocumentosFormService: TiposDocumentosFormService;
  let tiposDocumentosService: TiposDocumentosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TiposDocumentosUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TiposDocumentosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TiposDocumentosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tiposDocumentosFormService = TestBed.inject(TiposDocumentosFormService);
    tiposDocumentosService = TestBed.inject(TiposDocumentosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tiposDocumentos: ITiposDocumentos = { id: 456 };

      activatedRoute.data = of({ tiposDocumentos });
      comp.ngOnInit();

      expect(comp.tiposDocumentos).toEqual(tiposDocumentos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITiposDocumentos>>();
      const tiposDocumentos = { id: 123 };
      jest.spyOn(tiposDocumentosFormService, 'getTiposDocumentos').mockReturnValue(tiposDocumentos);
      jest.spyOn(tiposDocumentosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tiposDocumentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tiposDocumentos }));
      saveSubject.complete();

      // THEN
      expect(tiposDocumentosFormService.getTiposDocumentos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tiposDocumentosService.update).toHaveBeenCalledWith(expect.objectContaining(tiposDocumentos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITiposDocumentos>>();
      const tiposDocumentos = { id: 123 };
      jest.spyOn(tiposDocumentosFormService, 'getTiposDocumentos').mockReturnValue({ id: null });
      jest.spyOn(tiposDocumentosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tiposDocumentos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tiposDocumentos }));
      saveSubject.complete();

      // THEN
      expect(tiposDocumentosFormService.getTiposDocumentos).toHaveBeenCalled();
      expect(tiposDocumentosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITiposDocumentos>>();
      const tiposDocumentos = { id: 123 };
      jest.spyOn(tiposDocumentosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tiposDocumentos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tiposDocumentosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
