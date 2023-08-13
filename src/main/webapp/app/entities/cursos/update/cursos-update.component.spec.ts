import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CursosFormService } from './cursos-form.service';
import { CursosService } from '../service/cursos.service';
import { ICursos } from '../cursos.model';
import { ITemas } from 'app/entities/temas/temas.model';
import { TemasService } from 'app/entities/temas/service/temas.service';

import { CursosUpdateComponent } from './cursos-update.component';

describe('Cursos Management Update Component', () => {
  let comp: CursosUpdateComponent;
  let fixture: ComponentFixture<CursosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cursosFormService: CursosFormService;
  let cursosService: CursosService;
  let temasService: TemasService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CursosUpdateComponent],
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
      .overrideTemplate(CursosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CursosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cursosFormService = TestBed.inject(CursosFormService);
    cursosService = TestBed.inject(CursosService);
    temasService = TestBed.inject(TemasService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Temas query and add missing value', () => {
      const cursos: ICursos = { id: 456 };
      const temas: ITemas = { id: 22047 };
      cursos.temas = temas;

      const temasCollection: ITemas[] = [{ id: 57250 }];
      jest.spyOn(temasService, 'query').mockReturnValue(of(new HttpResponse({ body: temasCollection })));
      const additionalTemas = [temas];
      const expectedCollection: ITemas[] = [...additionalTemas, ...temasCollection];
      jest.spyOn(temasService, 'addTemasToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cursos });
      comp.ngOnInit();

      expect(temasService.query).toHaveBeenCalled();
      expect(temasService.addTemasToCollectionIfMissing).toHaveBeenCalledWith(
        temasCollection,
        ...additionalTemas.map(expect.objectContaining)
      );
      expect(comp.temasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cursos: ICursos = { id: 456 };
      const temas: ITemas = { id: 21253 };
      cursos.temas = temas;

      activatedRoute.data = of({ cursos });
      comp.ngOnInit();

      expect(comp.temasSharedCollection).toContain(temas);
      expect(comp.cursos).toEqual(cursos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICursos>>();
      const cursos = { id: 123 };
      jest.spyOn(cursosFormService, 'getCursos').mockReturnValue(cursos);
      jest.spyOn(cursosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cursos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cursos }));
      saveSubject.complete();

      // THEN
      expect(cursosFormService.getCursos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cursosService.update).toHaveBeenCalledWith(expect.objectContaining(cursos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICursos>>();
      const cursos = { id: 123 };
      jest.spyOn(cursosFormService, 'getCursos').mockReturnValue({ id: null });
      jest.spyOn(cursosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cursos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cursos }));
      saveSubject.complete();

      // THEN
      expect(cursosFormService.getCursos).toHaveBeenCalled();
      expect(cursosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICursos>>();
      const cursos = { id: 123 };
      jest.spyOn(cursosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cursos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cursosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
