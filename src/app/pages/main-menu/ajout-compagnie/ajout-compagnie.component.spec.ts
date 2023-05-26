import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutCompagnieComponent } from './ajout-compagnie.component';

describe('AjoutCompagnieComponent', () => {
  let component: AjoutCompagnieComponent;
  let fixture: ComponentFixture<AjoutCompagnieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AjoutCompagnieComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AjoutCompagnieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
