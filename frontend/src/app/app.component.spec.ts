import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http'; 
import { AppComponent } from './app.component';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [],
      imports: [HttpClientModule, AppComponent], 
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  }));

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });
});
