import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreationCaserneComponent } from './creation-caserne.component';

describe('CreationCaserneComponent', () => {
  let component: CreationCaserneComponent;
  let fixture: ComponentFixture<CreationCaserneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreationCaserneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreationCaserneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
