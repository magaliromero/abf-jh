import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RegistroClasesDetailComponent } from './registro-clases-detail.component';

describe('RegistroClases Management Detail Component', () => {
  let comp: RegistroClasesDetailComponent;
  let fixture: ComponentFixture<RegistroClasesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistroClasesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ registroClases: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RegistroClasesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RegistroClasesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load registroClases on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.registroClases).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
