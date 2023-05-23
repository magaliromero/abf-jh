import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MaterialesDetailComponent } from './materiales-detail.component';

describe('Materiales Management Detail Component', () => {
  let comp: MaterialesDetailComponent;
  let fixture: ComponentFixture<MaterialesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MaterialesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ materiales: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(MaterialesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MaterialesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load materiales on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.materiales).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
