import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastrarPropriedade } from './cadastrar-propriedade';

describe('CadastrarPropriedade', () => {
  let component: CadastrarPropriedade;
  let fixture: ComponentFixture<CadastrarPropriedade>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CadastrarPropriedade]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CadastrarPropriedade);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
