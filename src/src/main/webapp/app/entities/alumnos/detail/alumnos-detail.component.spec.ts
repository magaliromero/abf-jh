import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AlumnosDetailComponent } from './alumnos-detail.component';

describe('Alumnos Management Detail Component', () => {
  let comp: AlumnosDetailComponent;
  let fixture: ComponentFixture<AlumnosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AlumnosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ alumnos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AlumnosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AlumnosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load alumnos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.alumnos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
