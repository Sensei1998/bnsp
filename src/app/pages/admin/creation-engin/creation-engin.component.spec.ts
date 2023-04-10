import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreationEnginComponent } from './creation-engin.component';

describe('CreationEnginComponent', () => {
  let component: CreationEnginComponent;
  let fixture: ComponentFixture<CreationEnginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreationEnginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreationEnginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
