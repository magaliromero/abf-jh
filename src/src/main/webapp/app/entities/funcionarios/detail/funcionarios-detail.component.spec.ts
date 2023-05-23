import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FuncionariosDetailComponent } from './funcionarios-detail.component';

describe('Funcionarios Management Detail Component', () => {
  let comp: FuncionariosDetailComponent;
  let fixture: ComponentFixture<FuncionariosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FuncionariosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ funcionarios: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FuncionariosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FuncionariosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load funcionarios on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.funcionarios).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
