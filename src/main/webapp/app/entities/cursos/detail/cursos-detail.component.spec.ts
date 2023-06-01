import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CursosDetailComponent } from './cursos-detail.component';

describe('Cursos Management Detail Component', () => {
  let comp: CursosDetailComponent;
  let fixture: ComponentFixture<CursosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CursosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cursos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CursosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CursosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cursos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cursos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
