import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreationAgentComponent } from './creation-agent.component';

describe('CreationAgentComponent', () => {
  let component: CreationAgentComponent;
  let fixture: ComponentFixture<CreationAgentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreationAgentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreationAgentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
