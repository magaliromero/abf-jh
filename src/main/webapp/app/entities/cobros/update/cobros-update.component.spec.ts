import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CobrosFormService } from './cobros-form.service';
import { CobrosService } from '../service/cobros.service';
import { ICobros } from '../cobros.model';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';

import { CobrosUpdateComponent } from './cobros-update.component';

describe('Cobros Management Update Component', () => {
  let comp: CobrosUpdateComponent;
  let fixture: ComponentFixture<CobrosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cobrosFormService: CobrosFormService;
  let cobrosService: CobrosService;
  let alumnosService: AlumnosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CobrosUpdateComponent],
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
      .overrideTemplate(CobrosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CobrosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cobrosFormService = TestBed.inject(CobrosFormService);
    cobrosService = TestBed.inject(CobrosService);
    alumnosService = TestBed.inject(AlumnosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Alumnos query and add missing value', () => {
      const cobros: ICobros = { id: 456 };
      const alumnos: IAlumnos = { id: 20188 };
      cobros.alumnos = alumnos;

      const alumnosCollection: IAlumnos[] = [{ id: 31669 }];
      jest.spyOn(alumnosService, 'query').mockReturnValue(of(new HttpResponse({ body: alumnosCollection })));
      const additionalAlumnos = [alumnos];
      const expectedCollection: IAlumnos[] = [...additionalAlumnos, ...alumnosCollection];
      jest.spyOn(alumnosService, 'addAlumnosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cobros });
      comp.ngOnInit();

      expect(alumnosService.query).toHaveBeenCalled();
      expect(alumnosService.addAlumnosToCollectionIfMissing).toHaveBeenCalledWith(
        alumnosCollection,
        ...additionalAlumnos.map(expect.objectContaining)
      );
      expect(comp.alumnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cobros: ICobros = { id: 456 };
      const alumnos: IAlumnos = { id: 58261 };
      cobros.alumnos = alumnos;

      activatedRoute.data = of({ cobros });
      comp.ngOnInit();

      expect(comp.alumnosSharedCollection).toContain(alumnos);
      expect(comp.cobros).toEqual(cobros);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICobros>>();
      const cobros = { id: 123 };
      jest.spyOn(cobrosFormService, 'getCobros').mockReturnValue(cobros);
      jest.spyOn(cobrosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cobros });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cobros }));
      saveSubject.complete();

      // THEN
      expect(cobrosFormService.getCobros).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cobrosService.update).toHaveBeenCalledWith(expect.objectContaining(cobros));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICobros>>();
      const cobros = { id: 123 };
      jest.spyOn(cobrosFormService, 'getCobros').mockReturnValue({ id: null });
      jest.spyOn(cobrosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cobros: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cobros }));
      saveSubject.complete();

      // THEN
      expect(cobrosFormService.getCobros).toHaveBeenCalled();
      expect(cobrosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICobros>>();
      const cobros = { id: 123 };
      jest.spyOn(cobrosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cobros });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cobrosService.update).toHaveBeenCalled();
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
  });
});
