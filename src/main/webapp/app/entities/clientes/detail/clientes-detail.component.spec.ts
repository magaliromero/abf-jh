import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClientesDetailComponent } from './clientes-detail.component';

describe('Clientes Management Detail Component', () => {
  let comp: ClientesDetailComponent;
  let fixture: ComponentFixture<ClientesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClientesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ clientes: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClientesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClientesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load clientes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.clientes).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
