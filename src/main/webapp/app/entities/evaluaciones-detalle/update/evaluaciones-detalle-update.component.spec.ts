import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EvaluacionesDetalleFormService } from './evaluaciones-detalle-form.service';
import { EvaluacionesDetalleService } from '../service/evaluaciones-detalle.service';
import { IEvaluacionesDetalle } from '../evaluaciones-detalle.model';
import { IEvaluaciones } from 'app/entities/evaluaciones/evaluaciones.model';
import { EvaluacionesService } from 'app/entities/evaluaciones/service/evaluaciones.service';
import { ITemas } from 'app/entities/temas/temas.model';
import { TemasService } from 'app/entities/temas/service/temas.service';

import { EvaluacionesDetalleUpdateComponent } from './evaluaciones-detalle-update.component';

describe('EvaluacionesDetalle Management Update Component', () => {
  let comp: EvaluacionesDetalleUpdateComponent;
  let fixture: ComponentFixture<EvaluacionesDetalleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evaluacionesDetalleFormService: EvaluacionesDetalleFormService;
  let evaluacionesDetalleService: EvaluacionesDetalleService;
  let evaluacionesService: EvaluacionesService;
  let temasService: TemasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EvaluacionesDetalleUpdateComponent],
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
      .overrideTemplate(EvaluacionesDetalleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvaluacionesDetalleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evaluacionesDetalleFormService = TestBed.inject(EvaluacionesDetalleFormService);
    evaluacionesDetalleService = TestBed.inject(EvaluacionesDetalleService);
    evaluacionesService = TestBed.inject(EvaluacionesService);
    temasService = TestBed.inject(TemasService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Evaluaciones query and add missing value', () => {
      const evaluacionesDetalle: IEvaluacionesDetalle = { id: 456 };
      const evaluaciones: IEvaluaciones = { id: 18704 };
      evaluacionesDetalle.evaluaciones = evaluaciones;

      const evaluacionesCollection: IEvaluaciones[] = [{ id: 45463 }];
      jest.spyOn(evaluacionesService, 'query').mockReturnValue(of(new HttpResponse({ body: evaluacionesCollection })));
      const additionalEvaluaciones = [evaluaciones];
      const expectedCollection: IEvaluaciones[] = [...additionalEvaluaciones, ...evaluacionesCollection];
      jest.spyOn(evaluacionesService, 'addEvaluacionesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluacionesDetalle });
      comp.ngOnInit();

      expect(evaluacionesService.query).toHaveBeenCalled();
      expect(evaluacionesService.addEvaluacionesToCollectionIfMissing).toHaveBeenCalledWith(
        evaluacionesCollection,
        ...additionalEvaluaciones.map(expect.objectContaining)
      );
      expect(comp.evaluacionesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Temas query and add missing value', () => {
      const evaluacionesDetalle: IEvaluacionesDetalle = { id: 456 };
      const temas: ITemas = { id: 20732 };
      evaluacionesDetalle.temas = temas;

      const temasCollection: ITemas[] = [{ id: 9847 }];
      jest.spyOn(temasService, 'query').mockReturnValue(of(new HttpResponse({ body: temasCollection })));
      const additionalTemas = [temas];
      const expectedCollection: ITemas[] = [...additionalTemas, ...temasCollection];
      jest.spyOn(temasService, 'addTemasToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluacionesDetalle });
      comp.ngOnInit();

      expect(temasService.query).toHaveBeenCalled();
      expect(temasService.addTemasToCollectionIfMissing).toHaveBeenCalledWith(
        temasCollection,
        ...additionalTemas.map(expect.objectContaining)
      );
      expect(comp.temasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evaluacionesDetalle: IEvaluacionesDetalle = { id: 456 };
      const evaluaciones: IEvaluaciones = { id: 11157 };
      evaluacionesDetalle.evaluaciones = evaluaciones;
      const temas: ITemas = { id: 26776 };
      evaluacionesDetalle.temas = temas;

      activatedRoute.data = of({ evaluacionesDetalle });
      comp.ngOnInit();

      expect(comp.evaluacionesSharedCollection).toContain(evaluaciones);
      expect(comp.temasSharedCollection).toContain(temas);
      expect(comp.evaluacionesDetalle).toEqual(evaluacionesDetalle);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluacionesDetalle>>();
      const evaluacionesDetalle = { id: 123 };
      jest.spyOn(evaluacionesDetalleFormService, 'getEvaluacionesDetalle').mockReturnValue(evaluacionesDetalle);
      jest.spyOn(evaluacionesDetalleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluacionesDetalle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluacionesDetalle }));
      saveSubject.complete();

      // THEN
      expect(evaluacionesDetalleFormService.getEvaluacionesDetalle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evaluacionesDetalleService.update).toHaveBeenCalledWith(expect.objectContaining(evaluacionesDetalle));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluacionesDetalle>>();
      const evaluacionesDetalle = { id: 123 };
      jest.spyOn(evaluacionesDetalleFormService, 'getEvaluacionesDetalle').mockReturnValue({ id: null });
      jest.spyOn(evaluacionesDetalleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluacionesDetalle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluacionesDetalle }));
      saveSubject.complete();

      // THEN
      expect(evaluacionesDetalleFormService.getEvaluacionesDetalle).toHaveBeenCalled();
      expect(evaluacionesDetalleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluacionesDetalle>>();
      const evaluacionesDetalle = { id: 123 };
      jest.spyOn(evaluacionesDetalleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluacionesDetalle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evaluacionesDetalleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEvaluaciones', () => {
      it('Should forward to evaluacionesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(evaluacionesService, 'compareEvaluaciones');
        comp.compareEvaluaciones(entity, entity2);
        expect(evaluacionesService.compareEvaluaciones).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTemas', () => {
      it('Should forward to temasService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(temasService, 'compareTemas');
        comp.compareTemas(entity, entity2);
        expect(temasService.compareTemas).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
