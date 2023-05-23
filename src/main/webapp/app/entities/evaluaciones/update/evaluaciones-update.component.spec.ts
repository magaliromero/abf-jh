import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EvaluacionesFormService } from './evaluaciones-form.service';
import { EvaluacionesService } from '../service/evaluaciones.service';
import { IEvaluaciones } from '../evaluaciones.model';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';
import { IFuncionarios } from 'app/entities/funcionarios/funcionarios.model';
import { FuncionariosService } from 'app/entities/funcionarios/service/funcionarios.service';

import { EvaluacionesUpdateComponent } from './evaluaciones-update.component';

describe('Evaluaciones Management Update Component', () => {
  let comp: EvaluacionesUpdateComponent;
  let fixture: ComponentFixture<EvaluacionesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evaluacionesFormService: EvaluacionesFormService;
  let evaluacionesService: EvaluacionesService;
  let alumnosService: AlumnosService;
  let funcionariosService: FuncionariosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EvaluacionesUpdateComponent],
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
      .overrideTemplate(EvaluacionesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvaluacionesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evaluacionesFormService = TestBed.inject(EvaluacionesFormService);
    evaluacionesService = TestBed.inject(EvaluacionesService);
    alumnosService = TestBed.inject(AlumnosService);
    funcionariosService = TestBed.inject(FuncionariosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Alumnos query and add missing value', () => {
      const evaluaciones: IEvaluaciones = { id: 456 };
      const alumnos: IAlumnos = { id: 93573 };
      evaluaciones.alumnos = alumnos;

      const alumnosCollection: IAlumnos[] = [{ id: 63763 }];
      jest.spyOn(alumnosService, 'query').mockReturnValue(of(new HttpResponse({ body: alumnosCollection })));
      const additionalAlumnos = [alumnos];
      const expectedCollection: IAlumnos[] = [...additionalAlumnos, ...alumnosCollection];
      jest.spyOn(alumnosService, 'addAlumnosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluaciones });
      comp.ngOnInit();

      expect(alumnosService.query).toHaveBeenCalled();
      expect(alumnosService.addAlumnosToCollectionIfMissing).toHaveBeenCalledWith(
        alumnosCollection,
        ...additionalAlumnos.map(expect.objectContaining)
      );
      expect(comp.alumnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Funcionarios query and add missing value', () => {
      const evaluaciones: IEvaluaciones = { id: 456 };
      const funcionarios: IFuncionarios = { id: 6804 };
      evaluaciones.funcionarios = funcionarios;

      const funcionariosCollection: IFuncionarios[] = [{ id: 94975 }];
      jest.spyOn(funcionariosService, 'query').mockReturnValue(of(new HttpResponse({ body: funcionariosCollection })));
      const additionalFuncionarios = [funcionarios];
      const expectedCollection: IFuncionarios[] = [...additionalFuncionarios, ...funcionariosCollection];
      jest.spyOn(funcionariosService, 'addFuncionariosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluaciones });
      comp.ngOnInit();

      expect(funcionariosService.query).toHaveBeenCalled();
      expect(funcionariosService.addFuncionariosToCollectionIfMissing).toHaveBeenCalledWith(
        funcionariosCollection,
        ...additionalFuncionarios.map(expect.objectContaining)
      );
      expect(comp.funcionariosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evaluaciones: IEvaluaciones = { id: 456 };
      const alumnos: IAlumnos = { id: 88866 };
      evaluaciones.alumnos = alumnos;
      const funcionarios: IFuncionarios = { id: 31409 };
      evaluaciones.funcionarios = funcionarios;

      activatedRoute.data = of({ evaluaciones });
      comp.ngOnInit();

      expect(comp.alumnosSharedCollection).toContain(alumnos);
      expect(comp.funcionariosSharedCollection).toContain(funcionarios);
      expect(comp.evaluaciones).toEqual(evaluaciones);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluaciones>>();
      const evaluaciones = { id: 123 };
      jest.spyOn(evaluacionesFormService, 'getEvaluaciones').mockReturnValue(evaluaciones);
      jest.spyOn(evaluacionesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluaciones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluaciones }));
      saveSubject.complete();

      // THEN
      expect(evaluacionesFormService.getEvaluaciones).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evaluacionesService.update).toHaveBeenCalledWith(expect.objectContaining(evaluaciones));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluaciones>>();
      const evaluaciones = { id: 123 };
      jest.spyOn(evaluacionesFormService, 'getEvaluaciones').mockReturnValue({ id: null });
      jest.spyOn(evaluacionesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluaciones: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluaciones }));
      saveSubject.complete();

      // THEN
      expect(evaluacionesFormService.getEvaluaciones).toHaveBeenCalled();
      expect(evaluacionesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluaciones>>();
      const evaluaciones = { id: 123 };
      jest.spyOn(evaluacionesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluaciones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evaluacionesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAlumnos', () => {
      it('Should forward to alumnosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(alumnosService, 'compareAlumnos');
        comp.compareAlumnos(entity, entity2);
        expect(alumnosService.compareAlumnos).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareFuncionarios', () => {
      it('Should forward to funcionariosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(funcionariosService, 'compareFuncionarios');
        comp.compareFuncionarios(entity, entity2);
        expect(funcionariosService.compareFuncionarios).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
