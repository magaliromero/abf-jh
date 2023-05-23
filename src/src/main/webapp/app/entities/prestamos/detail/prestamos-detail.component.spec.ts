import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrestamosDetailComponent } from './prestamos-detail.component';

describe('Prestamos Management Detail Component', () => {
  let comp: PrestamosDetailComponent;
  let fixture: ComponentFixture<PrestamosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrestamosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prestamos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrestamosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrestamosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prestamos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prestamos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
