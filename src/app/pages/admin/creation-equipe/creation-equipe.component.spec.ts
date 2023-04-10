import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreationEquipeComponent } from './creation-equipe.component';

describe('CreationEquipeComponent', () => {
  let component: CreationEquipeComponent;
  let fixture: ComponentFixture<CreationEquipeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreationEquipeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreationEquipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
