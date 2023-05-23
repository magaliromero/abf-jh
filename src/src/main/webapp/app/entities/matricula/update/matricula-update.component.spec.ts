import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MatriculaFormService } from './matricula-form.service';
import { MatriculaService } from '../service/matricula.service';
import { IMatricula } from '../matricula.model';
import { IAlumnos } from 'app/entities/alumnos/alumnos.model';
import { AlumnosService } from 'app/entities/alumnos/service/alumnos.service';

import { MatriculaUpdateComponent } from './matricula-update.component';

describe('Matricula Management Update Component', () => {
  let comp: MatriculaUpdateComponent;
  let fixture: ComponentFixture<MatriculaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let matriculaFormService: MatriculaFormService;
  let matriculaService: MatriculaService;
  let alumnosService: AlumnosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MatriculaUpdateComponent],
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
      .overrideTemplate(MatriculaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MatriculaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    matriculaFormService = TestBed.inject(MatriculaFormService);
    matriculaService = TestBed.inject(MatriculaService);
    alumnosService = TestBed.inject(AlumnosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Alumnos query and add missing value', () => {
      const matricula: IMatricula = { id: 456 };
      const alumno: IAlumnos = { id: 33220 };
      matricula.alumno = alumno;

      const alumnosCollection: IAlumnos[] = [{ id: 26642 }];
      jest.spyOn(alumnosService, 'query').mockReturnValue(of(new HttpResponse({ body: alumnosCollection })));
      const additionalAlumnos = [alumno];
      const expectedCollection: IAlumnos[] = [...additionalAlumnos, ...alumnosCollection];
      jest.spyOn(alumnosService, 'addAlumnosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      expect(alumnosService.query).toHaveBeenCalled();
      expect(alumnosService.addAlumnosToCollectionIfMissing).toHaveBeenCalledWith(
        alumnosCollection,
        ...additionalAlumnos.map(expect.objectContaining)
      );
      expect(comp.alumnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const matricula: IMatricula = { id: 456 };
      const alumno: IAlumnos = { id: 55885 };
      matricula.alumno = alumno;

      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      expect(comp.alumnosSharedCollection).toContain(alumno);
      expect(comp.matricula).toEqual(matricula);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatricula>>();
      const matricula = { id: 123 };
      jest.spyOn(matriculaFormService, 'getMatricula').mockReturnValue(matricula);
      jest.spyOn(matriculaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matricula }));
      saveSubject.complete();

      // THEN
      expect(matriculaFormService.getMatricula).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(matriculaService.update).toHaveBeenCalledWith(expect.objectContaining(matricula));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatricula>>();
      const matricula = { id: 123 };
      jest.spyOn(matriculaFormService, 'getMatricula').mockReturnValue({ id: null });
      jest.spyOn(matriculaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matricula: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: matricula }));
      saveSubject.complete();

      // THEN
      expect(matriculaFormService.getMatricula).toHaveBeenCalled();
      expect(matriculaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMatricula>>();
      const matricula = { id: 123 };
      jest.spyOn(matriculaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ matricula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(matriculaService.update).toHaveBeenCalled();
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
