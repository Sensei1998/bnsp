import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreationProgramComponent } from './creation-program.component';

describe('CreationProgramComponent', () => {
  let component: CreationProgramComponent;
  let fixture: ComponentFixture<CreationProgramComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreationProgramComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreationProgramComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
