import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TiposDocumentosDetailComponent } from './tipos-documentos-detail.component';

describe('TiposDocumentos Management Detail Component', () => {
  let comp: TiposDocumentosDetailComponent;
  let fixture: ComponentFixture<TiposDocumentosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TiposDocumentosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tiposDocumentos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TiposDocumentosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TiposDocumentosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tiposDocumentos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tiposDocumentos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
