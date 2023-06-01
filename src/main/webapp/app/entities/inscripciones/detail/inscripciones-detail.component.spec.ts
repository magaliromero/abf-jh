import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InscripcionesDetailComponent } from './inscripciones-detail.component';

describe('Inscripciones Management Detail Component', () => {
  let comp: InscripcionesDetailComponent;
  let fixture: ComponentFixture<InscripcionesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InscripcionesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inscripciones: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InscripcionesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InscripcionesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inscripciones on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inscripciones).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
