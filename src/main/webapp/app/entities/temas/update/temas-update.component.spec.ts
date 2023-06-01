import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TemasFormService } from './temas-form.service';
import { TemasService } from '../service/temas.service';
import { ITemas } from '../temas.model';
import { ICursos } from 'app/entities/cursos/cursos.model';
import { CursosService } from 'app/entities/cursos/service/cursos.service';

import { TemasUpdateComponent } from './temas-update.component';

describe('Temas Management Update Component', () => {
  let comp: TemasUpdateComponent;
  let fixture: ComponentFixture<TemasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let temasFormService: TemasFormService;
  let temasService: TemasService;
  let cursosService: CursosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TemasUpdateComponent],
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
      .overrideTemplate(TemasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TemasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    temasFormService = TestBed.inject(TemasFormService);
    temasService = TestBed.inject(TemasService);
    cursosService = TestBed.inject(CursosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cursos query and add missing value', () => {
      const temas: ITemas = { id: 456 };
      const cursos: ICursos = { id: 83927 };
      temas.cursos = cursos;

      const cursosCollection: ICursos[] = [{ id: 2905 }];
      jest.spyOn(cursosService, 'query').mockReturnValue(of(new HttpResponse({ body: cursosCollection })));
      const additionalCursos = [cursos];
      const expectedCollection: ICursos[] = [...additionalCursos, ...cursosCollection];
      jest.spyOn(cursosService, 'addCursosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ temas });
      comp.ngOnInit();

      expect(cursosService.query).toHaveBeenCalled();
      expect(cursosService.addCursosToCollectionIfMissing).toHaveBeenCalledWith(
        cursosCollection,
        ...additionalCursos.map(expect.objectContaining)
      );
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const temas: ITemas = { id: 456 };
      const cursos: ICursos = { id: 56821 };
      temas.cursos = cursos;

      activatedRoute.data = of({ temas });
      comp.ngOnInit();

      expect(comp.cursosSharedCollection).toContain(cursos);
      expect(comp.temas).toEqual(temas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITemas>>();
      const temas = { id: 123 };
      jest.spyOn(temasFormService, 'getTemas').mockReturnValue(temas);
      jest.spyOn(temasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ temas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: temas }));
      saveSubject.complete();

      // THEN
      expect(temasFormService.getTemas).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(temasService.update).toHaveBeenCalledWith(expect.objectContaining(temas));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITemas>>();
      const temas = { id: 123 };
      jest.spyOn(temasFormService, 'getTemas').mockReturnValue({ id: null });
      jest.spyOn(temasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ temas: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: temas }));
      saveSubject.complete();

      // THEN
      expect(temasFormService.getTemas).toHaveBeenCalled();
      expect(temasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITemas>>();
      const temas = { id: 123 };
      jest.spyOn(temasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ temas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(temasService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCursos', () => {
      it('Should forward to cursosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cursosService, 'compareCursos');
        comp.compareCursos(entity, entity2);
        expect(cursosService.compareCursos).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
