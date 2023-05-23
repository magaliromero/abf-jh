import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MaterialesFormService } from './materiales-form.service';
import { MaterialesService } from '../service/materiales.service';
import { IMateriales } from '../materiales.model';

import { MaterialesUpdateComponent } from './materiales-update.component';

describe('Materiales Management Update Component', () => {
  let comp: MaterialesUpdateComponent;
  let fixture: ComponentFixture<MaterialesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let materialesFormService: MaterialesFormService;
  let materialesService: MaterialesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MaterialesUpdateComponent],
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
      .overrideTemplate(MaterialesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaterialesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    materialesFormService = TestBed.inject(MaterialesFormService);
    materialesService = TestBed.inject(MaterialesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const materiales: IMateriales = { id: 456 };

      activatedRoute.data = of({ materiales });
      comp.ngOnInit();

      expect(comp.materiales).toEqual(materiales);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateriales>>();
      const materiales = { id: 123 };
      jest.spyOn(materialesFormService, 'getMateriales').mockReturnValue(materiales);
      jest.spyOn(materialesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materiales });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materiales }));
      saveSubject.complete();

      // THEN
      expect(materialesFormService.getMateriales).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(materialesService.update).toHaveBeenCalledWith(expect.objectContaining(materiales));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateriales>>();
      const materiales = { id: 123 };
      jest.spyOn(materialesFormService, 'getMateriales').mockReturnValue({ id: null });
      jest.spyOn(materialesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materiales: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: materiales }));
      saveSubject.complete();

      // THEN
      expect(materialesFormService.getMateriales).toHaveBeenCalled();
      expect(materialesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMateriales>>();
      const materiales = { id: 123 };
      jest.spyOn(materialesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ materiales });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(materialesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
