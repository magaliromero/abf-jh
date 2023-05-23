import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TemasDetailComponent } from './temas-detail.component';

describe('Temas Management Detail Component', () => {
  let comp: TemasDetailComponent;
  let fixture: ComponentFixture<TemasDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TemasDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ temas: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TemasDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TemasDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load temas on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.temas).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
