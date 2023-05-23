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

import { TemasUpdateComponent } from './temas-update.component';

describe('Temas Management Update Component', () => {
  let comp: TemasUpdateComponent;
  let fixture: ComponentFixture<TemasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let temasFormService: TemasFormService;
  let temasService: TemasService;

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const temas: ITemas = { id: 456 };

      activatedRoute.data = of({ temas });
      comp.ngOnInit();

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
});
