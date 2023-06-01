import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InscripcionesFormService } from './inscripciones-form.service';
import { InscripcionesService } from '../service/inscripciones.service';
import { IInscripciones } from '../inscripciones.model';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';
import { ICursos } from 'app/entities/cursos/cursos.model';
import { CursosService } from 'app/entities/cursos/service/cursos.service';

import { InscripcionesUpdateComponent } from './inscripciones-update.component';

describe('Inscripciones Management Update Component', () => {
  let comp: InscripcionesUpdateComponent;
  let fixture: ComponentFixture<InscripcionesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inscripcionesFormService: InscripcionesFormService;
  let inscripcionesService: InscripcionesService;
  let alumnosService: AlumnosService;
  let cursosService: CursosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InscripcionesUpdateComponent],
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
      .overrideTemplate(InscripcionesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InscripcionesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inscripcionesFormService = TestBed.inject(InscripcionesFormService);
    inscripcionesService = TestBed.inject(InscripcionesService);
    alumnosService = TestBed.inject(AlumnosService);
    cursosService = TestBed.inject(CursosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Alumnos query and add missing value', () => {
      const inscripciones: IInscripciones = { id: 456 };
      const alumnos: IAlumnos = { id: 60516 };
      inscripciones.alumnos = alumnos;

      const alumnosCollection: IAlumnos[] = [{ id: 23127 }];
      jest.spyOn(alumnosService, 'query').mockReturnValue(of(new HttpResponse({ body: alumnosCollection })));
      const additionalAlumnos = [alumnos];
      const expectedCollection: IAlumnos[] = [...additionalAlumnos, ...alumnosCollection];
      jest.spyOn(alumnosService, 'addAlumnosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscripciones });
      comp.ngOnInit();

      expect(alumnosService.query).toHaveBeenCalled();
      expect(alumnosService.addAlumnosToCollectionIfMissing).toHaveBeenCalledWith(
        alumnosCollection,
        ...additionalAlumnos.map(expect.objectContaining)
      );
      expect(comp.alumnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cursos query and add missing value', () => {
      const inscripciones: IInscripciones = { id: 456 };
      const cursos: ICursos = { id: 33650 };
      inscripciones.cursos = cursos;

      const cursosCollection: ICursos[] = [{ id: 14166 }];
      jest.spyOn(cursosService, 'query').mockReturnValue(of(new HttpResponse({ body: cursosCollection })));
      const additionalCursos = [cursos];
      const expectedCollection: ICursos[] = [...additionalCursos, ...cursosCollection];
      jest.spyOn(cursosService, 'addCursosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscripciones });
      comp.ngOnInit();

      expect(cursosService.query).toHaveBeenCalled();
      expect(cursosService.addCursosToCollectionIfMissing).toHaveBeenCalledWith(
        cursosCollection,
        ...additionalCursos.map(expect.objectContaining)
      );
      expect(comp.cursosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inscripciones: IInscripciones = { id: 456 };
      const alumnos: IAlumnos = { id: 94428 };
      inscripciones.alumnos = alumnos;
      const cursos: ICursos = { id: 10351 };
      inscripciones.cursos = cursos;

      activatedRoute.data = of({ inscripciones });
      comp.ngOnInit();

      expect(comp.alumnosSharedCollection).toContain(alumnos);
      expect(comp.cursosSharedCollection).toContain(cursos);
      expect(comp.inscripciones).toEqual(inscripciones);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscripciones>>();
      const inscripciones = { id: 123 };
      jest.spyOn(inscripcionesFormService, 'getInscripciones').mockReturnValue(inscripciones);
      jest.spyOn(inscripcionesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscripciones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscripciones }));
      saveSubject.complete();

      // THEN
      expect(inscripcionesFormService.getInscripciones).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inscripcionesService.update).toHaveBeenCalledWith(expect.objectContaining(inscripciones));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscripciones>>();
      const inscripciones = { id: 123 };
      jest.spyOn(inscripcionesFormService, 'getInscripciones').mockReturnValue({ id: null });
      jest.spyOn(inscripcionesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscripciones: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscripciones }));
      saveSubject.complete();

      // THEN
      expect(inscripcionesFormService.getInscripciones).toHaveBeenCalled();
      expect(inscripcionesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscripciones>>();
      const inscripciones = { id: 123 };
      jest.spyOn(inscripcionesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscripciones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inscripcionesService.update).toHaveBeenCalled();
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
